package white.goo.constant;

import lombok.Getter;
import lombok.Setter;
import white.goo.core.MySecurityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ValidateContext {

    private List<Map<String, List<String[]>>> param;

    private Operator opt;

    private MySecurityManager securityManager;

    public ValidateContext(Operator opt) {
        this.param = new ArrayList<>();
        this.opt = opt;
    }
}
