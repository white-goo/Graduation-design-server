package white.goo.serivce;

import white.goo.constant.ValidateContext;

import java.util.List;
import java.util.Map;

public interface IValidator {

    boolean doValidate(ValidateContext ctx, Map<String,Object> requestParam, Map<String, List<String[]>> param);

}
