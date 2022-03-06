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
    private String name;
    private List<String> roleIds;
    private String permission;
    private List<String> courses;
    private Boolean isDelete = false;

}
