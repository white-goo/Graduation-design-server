package white.goo.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import white.goo.entity.User;
import white.goo.serivce.UserService;

@Slf4j
public class PassWordRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //这个方法一定要重写,后面Realm和Token匹配的时候会调用
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //这里没有做授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addStringPermission("");

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //这个地方用的Token是shiro自带的token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if(user == null){
            throw new AuthenticationException("用户名错误");
        }
        return new SimpleAuthenticationInfo(user,user.getPassword().toCharArray(),getName());
    }
}
