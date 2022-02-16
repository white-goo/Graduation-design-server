package white.goo.vo;

import lombok.Getter;
import lombok.Setter;
import white.goo.dto.BaseVO;

@Getter
@Setter
public class AuthVo extends BaseVO {

    private String authName;
    private String authShowName;
    private String moduleName;

}
