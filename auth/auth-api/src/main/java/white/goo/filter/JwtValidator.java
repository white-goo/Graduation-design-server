package white.goo.filter;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import white.goo.annonation.ValidatorDefine;
import white.goo.api.AbstractValidator;
import white.goo.api.IValidator;
import white.goo.constant.ValidateContext;
import white.goo.util.JwtUtil;
import white.goo.util.ThreadLocalUtil;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ValidatorDefine("jwt")
public class JwtValidator extends AbstractValidator<Map<String, Object>> {

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
                        String userId = JwtUtil.getUserId(token);
                        UserVO userInfo = UserUtil.getUserInfo(userId);
                        if(Objects.nonNull(userInfo)){
                            ThreadLocalUtil.set(token);
                            return true;
                        }
                    }
                    HttpServletResponse response = (HttpServletResponse) requestParam.get("response");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        return false;
    }
}
