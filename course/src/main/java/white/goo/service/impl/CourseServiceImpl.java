package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.entity.Course;
import white.goo.entity.Teacher;
import white.goo.repository.CourseMapper;
import white.goo.service.CourseService;
import white.goo.service.TeacherService;
import white.goo.util.EntityToVOMapper;
import white.goo.vo.CourseVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;

    @Override
    public List<CourseVO> listVO(Page<Course> page) {
        return super.page(page,buildCondition((Query<Course>) page)).getRecords().stream().map(item -> {
            CourseVO courseVO = new CourseVO();
            BeanUtil.copyProperties(item, courseVO);
            Teacher byId = teacherService.getById(item.getTeacherId());
            courseVO.setTeacher(byId);
            return courseVO;
        }).collect(Collectors.toList());
    }

    private<E> QueryWrapper<E> buildCondition(Query<E> query){
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        if(CollectionUtil.isEmpty(query.getConditions())){
            return queryWrapper;
        }
        query.getConditions().forEach(queryWrapper::like);
        return queryWrapper;
    }

    @Override
    public CourseVO loadById(String id) {
        Course course = super.getById(id);
        CourseVO courseVO = new CourseVO();
        EntityToVOMapper.getInstance().entityToVo(courseVO,course);
        return courseVO;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public boolean save(CourseVO courseVO) {
        Course course = new Course();
        BeanUtil.copyProperties(courseVO,course);
        boolean save = super.save(course);
        boolean save1 = true;
        if(Objects.nonNull(courseVO.getTeacher())){
            save1 = teacherService.save(courseVO.getTeacher());
        }
        return save && save1;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public Boolean update(CourseVO courseVO) {
        Course course = new Course();
        BeanUtil.copyProperties(courseVO,course);
        boolean update = super.update(course, new UpdateWrapper<Course>().eq("id", course.getId()));
        boolean update1 = true;
        if(Objects.nonNull(courseVO.getTeacher())){
            update1 = teacherService.update(courseVO.getTeacher(), new UpdateWrapper<Teacher>().eq("id", courseVO.getTeacher().getId()));
        }
        return update && update1;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public int delete(IdVO idVO) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>().eq("id", idVO.getId());
        Course one = getOne(queryWrapper);
        teacherService.delete(IdVO.of(one.getTeacherId()));
        return baseMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public int deleteBatch(List<IdVO> idVOList) {
        List<Course> courses = listByIds(idVOList.stream().map(IdVO::getId).collect(Collectors.toList()));
        List<String> teacherIds = courses.stream().map(Course::getTeacherId).filter(Strings::isNotEmpty).collect(Collectors.toList());
        teacherService.deleteBatch(teacherIds);
        return baseMapper.deleteBatchIds(courses.stream().map(Course::getId).collect(Collectors.toList()));
    }
}
