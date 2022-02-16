package white.goo.vo;

import lombok.Getter;
import lombok.Setter;
import white.goo.dto.BaseVO;
import white.goo.entity.Auth;

import java.util.List;

@Getter
@Setter
public class RoleVO extends BaseVO {

    private String roleName;
    private List<String> authId;
    private List<String> userId;

}
