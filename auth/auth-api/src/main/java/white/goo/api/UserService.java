package white.goo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.annonation.AuthValidator;
import white.goo.dto.Query;
import white.goo.entity.User;
import white.goo.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {

    @PostMapping("listVO")
    List<UserVO> listVO(@RequestBody Query<User> page);

    @PostMapping("addCourse")
    void addCourse(@RequestBody UserVO userVO);
}
