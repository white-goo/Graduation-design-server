package white.goo.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.entity.Course;
import white.goo.vo.CourseVO;

import java.util.List;

public interface CourseService extends IService<Course> {
    List<CourseVO> listVO(Query<Course> page);

    CourseVO loadById(String id);

    boolean save(CourseVO courseVO);

    Boolean update(CourseVO courseVO);

    int delete(IdVO idVO);

    int deleteBatch(List<IdVO> idVOList);
}
