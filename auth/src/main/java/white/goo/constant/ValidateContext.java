package white.goo.constant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ValidateContext {

    private List<Map<String, List<String[]>>> param;

    private Operator opt;

    public ValidateContext(Operator opt) {
        this.param = new ArrayList<>();
        this.opt = opt;
    }
}
