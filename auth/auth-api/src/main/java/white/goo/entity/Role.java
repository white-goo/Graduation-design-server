package white.goo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role extends BaseEntity {

    private String roleName;
    private String authId;
    private String userId;

}
