package white.goo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import white.goo.entity.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
