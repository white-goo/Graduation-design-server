package white.goo.serivce;

import com.alibaba.fastjson.JSONObject;
import white.goo.constant.ValidateContext;

import java.util.List;
import java.util.Map;

public interface IValidator {

    boolean doValidate(ValidateContext ctx, JSONObject requestParam, Map<String, List<String[]>> param);

}