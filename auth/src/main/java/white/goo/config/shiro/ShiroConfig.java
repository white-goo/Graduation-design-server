package white.goo.config.shiro;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {


    //首先,把自定义的Realm和ModularRealmAuthenticator配进来
    @Bean
    public Realm JwtRealm() {
        return new JwtRealm();
    }

    @Bean
    public Realm PassWordRealm() {
        return new PassWordRealm();
    }

    @Bean
    public UserModularRealmAuthenticator UserModularRealmAuthenticator() {

        UserModularRealmAuthenticator userModularRealmAuthenticator = new UserModularRealmAuthenticator();
        userModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return userModularRealmAuthenticator;
    }


    //配置ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //shiro 内置的过滤器
        /*
            anon:无需认证就能访问
            authc:必须认证才能访问
            user: 必须拥有 记住我 才能访问
            perms: 拥有对某个资源的权限才能访问
            role: 拥有某个角色的权限才能访问
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/auth/user/register", "anon");
        filterChainDefinitionMap.put("/auth/user/login", "anon");
        //静态资源一定要记得配置,不然会被shiro拦截
        filterChainDefinitionMap.put("/js/**/*", "anon");
        filterChainDefinitionMap.put("/css/**/*", "anon");
        filterChainDefinitionMap.put("/icon/**/*", "anon");
        filterChainDefinitionMap.put("/img/**/*", "anon");
        filterChainDefinitionMap.put("/fonts/**/*", "anon");

        //其余的所有请求都走我们的JwtFilter
        filterChainDefinitionMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        /**
         * 设置认证失败的路径
         * JwtFilter里返回false后重定向的路径就是这个,
         * 这是一个登录页的路径
         */
        shiroFilterFactoryBean.setLoginUrl("/auth/user/login");
        /**
         * 设置未经授权的路径
         */
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/user/login");

        /**
         * 设置自定义过滤器
         * 这里的key就是上面的过滤其名称,value就是自定义的JwtFilter
         */
        HashMap<String, Filter> objectObjectHashMap = new HashMap<>(1);
        objectObjectHashMap.put("jwt", new JwtFilter());

        shiroFilterFactoryBean.setFilters(objectObjectHashMap);

        return shiroFilterFactoryBean;
    }

    //配置DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(
            @Qualifier("JwtRealm") Realm jwtRealm,
            @Qualifier("PassWordRealm") Realm passWordRealm,
            @Qualifier("UserModularRealmAuthenticator") UserModularRealmAuthenticator userModularRealmAuthenticator) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        defaultWebSecurityManager.setAuthenticator(userModularRealmAuthenticator);

        //设置realm
        List<Realm> list = new ArrayList<>();
        list.add(jwtRealm);
        list.add(passWordRealm);
        defaultWebSecurityManager.setRealms(list);

        /**
         * 关闭shiro自带的session
         * shiro的session机制后续会补充
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);

        return defaultWebSecurityManager;
    }
}