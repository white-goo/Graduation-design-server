package white.goo.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.dto.Query;
import white.goo.entity.Role;
import white.goo.vo.RoleVO;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<RoleVO> listVO(Query<Role> page);

    void save(RoleVO roleVO);

    RoleVO load(String id);
}
