package white.goo.validator;

import white.goo.annonation.ValidatorDefine;
import white.goo.constant.ValidateContext;
import white.goo.serivce.IValidator;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import java.util.List;
import java.util.Map;

@ValidatorDefine("test")
public class TestValidator implements IValidator {
    @Override
    public boolean doValidate(ValidateContext ctx, Map<String, Object> requestParam, Map<String, List<String[]>> param) {
        UserVO currentUser = UserUtil.getCurrentUser();
        return "ceshi2".equals(currentUser.getUsername());
    }
}
