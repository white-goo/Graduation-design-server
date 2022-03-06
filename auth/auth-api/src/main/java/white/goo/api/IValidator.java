package white.goo.api;

import white.goo.constant.ValidateContext;

import java.util.List;
import java.util.Map;

public interface IValidator<T> {

    boolean doValidate(ValidateContext ctx, T requestParam, Map<String, List<String[]>> param);

}
