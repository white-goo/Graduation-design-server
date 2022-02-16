package white.goo.constant;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ValidateContext {

    private List<Map<String, List<String[]>>> param;

    private Operator opt;

    public ValidateContext(List<Map<String, List<String[]>>> param, Operator opt) {
        this.param = param;
        this.opt = opt;
    }
}
