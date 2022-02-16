package white.goo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import white.goo.annonation.ValidatorDefine;
import white.goo.constant.ValidateContext;
import white.goo.serivce.IValidator;
import white.goo.util.JwtUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@ValidatorDefine("roleValidator")
public class RoleValidator implements IValidator {

    @Override
    public boolean doValidate(ValidateContext ctx, JSONObject requestParam, Map<String, List<String[]>> param) {

        List<String[]> permissions = param.get("");

        String token = (String) SecurityUtils.getSubject().getPrincipal();
        JSONArray objects = JSON.parseArray(JwtUtil.getPermission(token));
        AtomicBoolean b = new AtomicBoolean(true);
        permissions.stream().map(item->item[0]).forEach(item->{
            if(!objects.contains(item)){
                b.set(false);
            }
        });
        return b.get();
    }
}
