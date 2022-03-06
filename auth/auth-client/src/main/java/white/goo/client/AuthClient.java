package white.goo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.dto.R;
import white.goo.entity.Auth;

import java.io.Serializable;

@FeignClient(name = "auth", path = "/auth", contextId = "auth")
public interface AuthClient {

    @PostMapping("remove")
    boolean remove(@RequestBody Serializable id);

    @PostMapping("save")
    boolean insert(@RequestBody Auth entity);

    @PostMapping("updateById")
    boolean update(@RequestBody Auth entity);

    @PostMapping("listByModuleName")
    R listByModuleName(@RequestBody String moduleName);

    @PostMapping("getByAuthName")
    Auth getByAuthName(@RequestBody String name);
}
