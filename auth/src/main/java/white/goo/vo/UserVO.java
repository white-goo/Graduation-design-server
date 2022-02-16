package white.goo.vo;

import lombok.Getter;
import lombok.Setter;
import white.goo.dto.BaseVO;

import java.util.List;

@Setter
@Getter
public class UserVO extends BaseVO {

    private String username;
    private String password;
    private List<String> permission;

}
