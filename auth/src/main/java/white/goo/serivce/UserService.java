package white.goo.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.dto.Query;
import white.goo.entity.User;
import white.goo.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {
    List<UserVO> listVO(Query<User> page);
}
