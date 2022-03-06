package white.goo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.api.RoleService;
import white.goo.dto.Query;
import white.goo.entity.Role;
import white.goo.vo.RoleVO;

import java.util.List;

@FeignClient(name = "auth", path = "/auth/role", contextId = "role")
public interface RoleClient {

    @PostMapping("listVO")
    List<RoleVO> listVO(@RequestBody Query<Role> page);

    @PostMapping("save")
    void save(@RequestBody RoleVO roleVO);

    @PostMapping("update")
    void update(@RequestBody RoleVO roleVO);

}
