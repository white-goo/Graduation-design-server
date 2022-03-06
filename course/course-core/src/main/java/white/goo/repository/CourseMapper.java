package white.goo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import white.goo.entity.Course;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
