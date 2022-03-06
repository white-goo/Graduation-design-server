package white.goo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import white.goo.annonation.ValidatorDefine;
import white.goo.api.IValidator;
import white.goo.constant.ValidateContext;
import white.goo.util.JwtUtil;
import white.goo.util.ThreadLocalUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@ValidatorDefine("roleValidator")
public class RoleValidator implements IValidator<Map<String, Object>> {

    @Override
    public boolean doValidate(ValidateContext ctx, Map<String,Object> requestParam, Map<String, List<String[]>> param) {

        List<String[]> permissions = param.get("");

        String token = ThreadLocalUtil.get();
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
