package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import white.goo.entity.Course;
import white.goo.entity.Teacher;
import white.goo.repository.CourseMapper;
import white.goo.service.CourseService;
import white.goo.service.TeacherService;
import white.goo.util.EntityToVOMapper;
import white.goo.vo.CourseVO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;

    @Override
    public List<CourseVO> listVO(Page<Course> page) {
        return super.page(page).getRecords().stream().map(item -> {
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
        EntityToVOMapper.getInstance().entityToVo(courseVO,course);
        return courseVO;
    }
}
