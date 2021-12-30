package white.goo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.entity.Course;
import white.goo.vo.CourseVO;

import java.util.List;

public interface CourseService extends IService<Course> {
    List<CourseVO> listVO();

    Object loadById(String id);
}
