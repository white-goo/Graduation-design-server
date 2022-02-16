package white.goo.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import white.goo.entity.User;
import white.goo.serivce.UserService;
import white.goo.util.JwtUtil;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {

        return token instanceof JwtToken;
    }

    //无授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {


        //这边还要做一个JWTToken,代码会放在下main
        //这里是从自定义的JWTToken里获取token
        String token = authenticationToken.getCredentials().toString();

        //对token的合法性进行校验
        if (!JwtUtil.verify(token)) {
            throw new AuthenticationException("认证异常");
        }

        //从token里获取用户名
        String userId = JwtUtil.getUserId(token);

        //这里应该根据用户名从数据库查询出User,此处为了方便直接new
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));

        if(user == null){
            throw new AuthenticationException("用户不存在");
        }

        //这里的校验规则和PassWordRealm的差不多,也放在下面讲
        return new SimpleAuthenticationInfo(token, token,getName());
    }
}
