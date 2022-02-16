package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import white.goo.util.DBUtil;
import white.goo.util.EntityToVOMapper;
import white.goo.vo.CourseVO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;

    @Override
    public List<CourseVO> listVO(Query<Course> page) {
        return super.page(page, DBUtil.buildCondition(page)).getRecords().stream().map(item -> {
            CourseVO courseVO = new CourseVO();
            BeanUtil.copyProperties(item, courseVO);
            Teacher byId = teacherService.getById(item.getTeacherId());
            courseVO.setTeacher(byId);
            return courseVO;
        }).collect(Collectors.toList());
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
        Course course = new Course();
        BeanUtil.copyProperties(courseVO, course, "id");
        course.setTeacherId(courseVO.getTeacher().getId());
        return super.save(course);
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
        return baseMapper.deleteById(idVO.getId());
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public int deleteBatch(List<IdVO> idVOList) {
        return baseMapper.deleteBatchIds(idVOList.stream().map(IdVO::getId).collect(Collectors.toList()));
    }
}
