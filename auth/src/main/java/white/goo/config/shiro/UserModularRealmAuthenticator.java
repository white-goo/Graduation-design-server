package white.goo.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {


    //这个类是确定传过来的token用哪个Realm进行验证的
    //JwtFilter中的login方法就会进到这里
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {

        //这里会验证realm是否是空的
        assertRealmsConfigured();

        //不为空就获取全部realm
        Collection<Realm> realms = getRealms();
        //用来放置登录相关的realm
        Collection<Realm> typeRealm = new ArrayList<>();


        try {
            //这个地方直接对传进来的token进行强转,因为系统内只有两个token,如果报了类型转换异常,就肯定是另外一个,所以只要在catch内强转成另外一个就行了
            JwtToken jwtToken = (JwtToken) authenticationToken;

            //如果是jwtToke,那就遍历所有的Realm,找到我们自定义的JwtRealm
            for (Realm realm : realms) {
                if(realm.getName().contains("Jwt")){
                    typeRealm.add(realm);
                }
            }
            //这个方法最终会进到JwtRealm的验证方法
            return doSingleRealmAuthentication(typeRealm.iterator().next(), jwtToken);

        } catch (ClassCastException  e) {

            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
            //跟上面一样,取出相应的Realm
            for (Realm realm : realms) {
                if(realm.getName().contains("PassWord")){
                    typeRealm.add(realm);
                }
            }

            //验证密码的方式可能有多中,所以提供了单Realm验证,和多Realm验证两种方式,本案例只有一个PassWordRealm
            if(typeRealm.size() == 1){
                return doSingleRealmAuthentication(typeRealm.iterator().next(), usernamePasswordToken);
            }else {
                return doMultiRealmAuthentication(typeRealm, usernamePasswordToken);
            }

        }

    }
}