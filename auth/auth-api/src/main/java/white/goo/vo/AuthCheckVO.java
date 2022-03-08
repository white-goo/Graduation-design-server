package white.goo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AuthCheckVO {

    private String key;
    private Map<String, Object> params;
    private String path;
    private String module;

}
