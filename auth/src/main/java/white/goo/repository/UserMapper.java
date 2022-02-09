package white.goo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import white.goo.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
