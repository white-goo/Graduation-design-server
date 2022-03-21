package white.goo.filter;

import com.google.common.base.Strings;
import white.goo.annonation.ValidatorDefine;
import white.goo.api.AbstractValidator;
import white.goo.api.IValidator;
import white.goo.config.RepeatedlyReadRequestWrapper;
import white.goo.constant.ValidateContext;
import white.goo.util.JwtUtil;

import java.util.List;
import java.util.Map;

@ValidatorDefine("backGroundJwtValidator")
public class BackGroundJwtValidator extends AbstractValidator<Map<String, Object>> {

    @Override
    public boolean doValidate(ValidateContext ctx, Map<String, Object> requestParam, Map<String, List<String[]>> param) {

        RepeatedlyReadRequestWrapper request = (RepeatedlyReadRequestWrapper) requestParam.get("request");
        String backGroundToken = request.getHeader("backGroundToken");
        if(Strings.isNullOrEmpty(backGroundToken)){
            return false;
        }
        return JwtUtil.verifyBackGroundToken(backGroundToken);
    }
}
