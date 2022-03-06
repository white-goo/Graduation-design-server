package white.goo.client;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.annonation.AuthValidator;
import white.goo.api.UserService;
import white.goo.dto.R;
import white.goo.entity.User;
import white.goo.vo.UserVO;

@FeignClient(name = "auth", path = "/auth/user", contextId = "user")
public interface UserClient {

    @PostMapping("getOne")
    User get(@RequestBody Wrapper<User> queryWrapper);

    @PostMapping("insert")
    boolean insert(@RequestBody User entity);

    @PostMapping("addCourse")
    R addCourse(@RequestBody UserVO userVO);

}
