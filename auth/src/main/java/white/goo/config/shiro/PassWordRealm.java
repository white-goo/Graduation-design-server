package white.goo.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import white.goo.entity.User;

@Slf4j
public class PassWordRealm extends AuthorizingRealm {

    //这个方法一定要重写,后面Realm和Token匹配的时候会调用
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //这里没有做授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //这个地方用的Token是shiro自带的token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        //拿到用户名去数据库查询,这里可以走数据库查,此处为了方便直接new,User的代码放在最后
        User user = new User("UserName","PassWord");
        if(user == null){
            throw new AuthenticationException("用户名错误");
        }

        //这个info返回值有三个参数,
        //第一个参数是user,可以不写,也可随意写
        //第三个参数是Realm的名字,他会作为Key,第一个参数作为value,一起被放到Map里面
        //第二个参数是校验参数,也就是密码,校验规则会放到下面

        return new SimpleAuthenticationInfo(null,"",getName());
    }
}
