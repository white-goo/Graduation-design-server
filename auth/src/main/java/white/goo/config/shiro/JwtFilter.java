package white.goo.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import white.goo.util.SpringUtil;
import white.goo.core.SecurityManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends FormAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //被拦截的请求会进入到这个方法,先从request中拿到cookie,因为我把登录成功后的token放到cookie里了,你可以根据情况进行调整
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        //拿到cookie后便利cookie拿到token
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().contains("Token")){
                    try {
                        //如果有token就进行Jwt验证,这个login方法最终会进到我们自定义的ModularRealmAuthenticator类中,最终会进到我们的JwtRealm中的验证方法
                        getSubject(request,response).login(new JwtToken(cookie.getValue()));
                        //如果JwtRealm中的验证一切正常,就返回true,反之,就捕获我们在JwtRealm里面的验证方法里面抛出的异常
                        SecurityManager securityManager = (SecurityManager)SpringUtil.getBean("securityManager");
                        return securityManager.doValidate();
                    } catch (AuthenticationException | IOException e) {
                        return false;
                    }
                }
            }
        }
        /**
         * 没有cookie 就返回false,会重定向到在ShiroConfig中配置的连接
         */
        return false;
    }
}