package white.goo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth extends BaseEntity {

    private String authName;
    private String authShowName;
    private String moduleName;

}
