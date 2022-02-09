package white.goo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import white.goo.entity.Auth;

@Mapper
public interface AuthMapper extends BaseMapper<Auth> {
}
