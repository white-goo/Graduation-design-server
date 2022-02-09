package white.goo.vo;

import lombok.Getter;
import lombok.Setter;
import white.goo.dto.BaseVO;

@Getter
@Setter
public class RoleVo extends BaseVO {

    private String roleName;
    private String authId;

}
