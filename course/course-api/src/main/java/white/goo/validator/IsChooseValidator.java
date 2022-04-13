package white.goo.validator;

import cn.hutool.core.collection.CollectionUtil;
import white.goo.annonation.ValidatorDefine;
import white.goo.api.AbstractValidator;
import white.goo.constant.ValidateContext;
import white.goo.dto.IdVO;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author shiyk
 * @date 2022/4/13
 */
@ValidatorDefine("isChooseValidator")
public class IsChooseValidator extends AbstractValidator<IdVO> {
    @Override
    public boolean doValidate(ValidateContext ctx, IdVO requestParam, Map<String, List<String[]>> param) {

        UserVO currentUser = UserUtil.getCurrentUser();
        List<String> courses = currentUser.getCourses();
        if(CollectionUtil.isNotEmpty(courses)){
            return !courses.contains(requestParam.getId());
        }
        return true;
    }
}
