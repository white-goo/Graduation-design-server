package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import white.goo.client.UserClient;
import white.goo.constant.CourseKeys;
import white.goo.constant.CourseRole;
import white.goo.constant.CourseStatusEnum;
import white.goo.dto.BaseVO;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.dto.R;
import white.goo.entity.Auth;
import white.goo.entity.Course;
import white.goo.entity.Teacher;
import white.goo.repository.CourseMapper;
import white.goo.service.CourseService;
import white.goo.service.TeacherService;
import white.goo.util.EntityToVOMapper;
import white.goo.util.UserUtil;
import white.goo.vo.CourseVO;
import white.goo.vo.UserVO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private UserClient userClient;

    @Override
    public List<CourseVO> listVO(Query<Course> page) {

        RMap<String, CourseVO> map = redissonClient.getMap(CourseKeys.COURSE);
        int count = count();
        if (map.size() == 0 || map.size() != count) {
            List<CourseVO> collect = refreshCache(page, map);
            if (collect != null) return collect;
            return null;
        }
        Collection<CourseVO> values = map.readAllValues();
        return values.stream().filter(item -> {
            Map<String, Object> conditions = page.getConditions();
            if (CollectionUtil.isNotEmpty(conditions)) {
                return conditions.get("course_name").equals(item.getCourseName());
            }
            return true;
        }).peek(item->{
            UserVO currentUser = UserUtil.getCurrentUser();
            List<String> list = JSON.parseArray(currentUser.getPermission(), String.class);
            List<String> courses = currentUser.getCourses();
            if(CollectionUtil.isNotEmpty(list)){
                item.setIsChoose((Objects.isNull(courses) || !courses.contains(item.getId())) && list.contains(CourseRole.COURSE_STUDENT));
            }
        }).sorted(Comparator.comparing(BaseVO::getId))
                .skip((page.getCurrent() - 1) * page.getSize())
                .limit(page.getSize()).collect(Collectors.toList());
    }

    private List<CourseVO> refreshCache(Query<Course> page, RMap<String, CourseVO> map) {
        RLock courseListLock = redissonClient.getRedLock(redissonClient.getLock("CourseListLock"));
        if (courseListLock.tryLock()) {
            List<Course> list = list();
            List<CourseVO> collect = list.stream().map(item -> {
                CourseVO courseVO = new CourseVO();
                BeanUtil.copyProperties(item, courseVO);
                Teacher byId = teacherService.getById(item.getTeacherId());
                courseVO.setTeacher(byId);
                map.put(courseVO.getId(), courseVO);
                return courseVO;
            }).sorted(Comparator.comparing(BaseVO::getId))
                    .skip(page.getCurrent() * page.getSize())
                    .limit(page.getSize()).collect(Collectors.toList());
            courseListLock.unlock();
            return collect;
        }
        return null;
    }

    @Override
    public CourseVO loadById(String id) {
        Course course = super.getById(id);
        CourseVO courseVO = new CourseVO();
        EntityToVOMapper.getInstance().entityToVo(courseVO, course);
        return courseVO;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public boolean save(CourseVO courseVO) {

        Date now = new Date();
        int compare = DateUtil.compare(now, courseVO.getDate());
        int compare1 = DateUtil.compare(now, courseVO.getEndDate());
        courseVO.setStatus(compare < 0 ? CourseStatusEnum.NOT_START : compare1 < 0 ? CourseStatusEnum.START : CourseStatusEnum.END);
        courseVO.setTeacher(teacherService.getById(courseVO.getTeacher().getId()));

        Course course = new Course();
        BeanUtil.copyProperties(courseVO, course, "id");
        course.setTeacherId(courseVO.getTeacher().getId());
        boolean save = super.save(course);

        RMap<Object, Object> map = redissonClient.getMap(CourseKeys.COURSE);
        courseVO.setId(course.getId());
        map.put(course.getId(), courseVO);
        RAtomicLong atomicLong = redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + course.getId());
        atomicLong.set(course.getCourseAmount());
        return save;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public Boolean update(CourseVO courseVO) {
        Course course = new Course();
        BeanUtil.copyProperties(courseVO, course, "id");
        course.setTeacherId(courseVO.getTeacher().getId());
        return super.update(course, new UpdateWrapper<Course>().eq("id", course.getId()));
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public int delete(IdVO idVO) {
        int i = baseMapper.deleteById(idVO.getId());
        RMap<Object, Object> map = redissonClient.getMap(CourseKeys.COURSE);
        map.remove(idVO.getId());
        redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + idVO.getId()).delete();
        return i;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public int deleteBatch(List<IdVO> idVOList) {
        RMap<Object, Object> map = redissonClient.getMap(CourseKeys.COURSE);
        List<String> ids = idVOList.stream().map(IdVO::getId).collect(Collectors.toList());
        map.fastRemove(ids);
        for (String id : ids) {
            redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + id).delete();
        }
        return baseMapper.deleteBatchIds(ids);
    }

    @Override
    public boolean chooseCourse(IdVO idVO) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + idVO.getId());
        long andDecrement = atomicLong.getAndDecrement();
        if (andDecrement > 0) {
            UserVO currentUser = UserUtil.getCurrentUser();
            if(CollectionUtil.isEmpty(currentUser.getCourses())){
                currentUser.setCourses(new ArrayList<>(10));
            }
            currentUser.getCourses().add(idVO.getId());
            userClient.addCourse(currentUser);
            Course byId = getById(idVO.getId());
            List<String> list = JSON.parseArray(byId.getStudents(), String.class);
            if(Objects.isNull(list)){
                list = new ArrayList<>(10);
            }
            list.add(idVO.getId());
            byId.setStudents(JSON.toJSONString(list));
            super.updateById(byId);
            return true;
        }else if(andDecrement == 0){
            atomicLong.delete();
            RMap<String, CourseVO> map = redissonClient.getMap(CourseKeys.COURSE);
            CourseVO courseVO = map.get(idVO.getId());
            courseVO.setStatus(CourseStatusEnum.FULL);
            map.put(idVO.getId(), courseVO);
            update(courseVO);
        }
        return false;
    }
}
