package white.goo.vo;

import lombok.Getter;
import lombok.Setter;
import white.goo.dto.BaseVO;

@Setter
@Getter
public class UserVo extends BaseVO {

    private String username;
    private String password;
    private String permission;

}
