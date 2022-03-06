package white.goo.filter;

import white.goo.annonation.ValidatorDefine;
import white.goo.api.IValidator;
import white.goo.constant.ValidateContext;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@ValidatorDefine("userUpdateValidator")
public class UserUpdateValidator implements IValidator<Map<String, Object>> {

    @Override
    public boolean doValidate(ValidateContext ctx, Map<String, Object> requestParam, Map<String, List<String[]>> param) {
        String id = (String) requestParam.get("id");
        UserVO currentUser = UserUtil.getCurrentUser();
        if(Objects.equals(currentUser.getId(),id)){
            return true;
        }
        return false;
    }
}

