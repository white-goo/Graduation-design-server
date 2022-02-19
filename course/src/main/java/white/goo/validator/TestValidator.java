package white.goo.validator;

import white.goo.annonation.ValidatorDefine;
import white.goo.constant.ValidateContext;
import white.goo.entity.User;
import white.goo.serivce.IValidator;
import white.goo.util.UserUtil;

import java.util.List;
import java.util.Map;

@ValidatorDefine("test")
public class TestValidator implements IValidator {
    @Override
    public boolean doValidate(ValidateContext ctx, Map<String, Object> requestParam, Map<String, List<String[]>> param) {
        User currentUser = UserUtil.getCurrentUser();
        return "ceshi2".equals(currentUser.getUsername());
    }
}
