package white.goo.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import white.goo.annonation.ValidatorDefine;
import white.goo.constant.ValidateContext;
import white.goo.serivce.IValidator;
import white.goo.util.JwtUtil;
import white.goo.util.ThreadLocalUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@ValidatorDefine("jwt")
public class JwtValidator implements IValidator<Map<String, Object>> {

    @Override
    public boolean doValidate(ValidateContext ctx, Map<String,Object> requestParam, Map<String, List<String[]>> param) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("Token")){
                    String token = cookie.getValue();
                    if (JwtUtil.verify(token)) {
                        ThreadLocalUtil.set(token);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
