package white.goo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import white.goo.core.MySecurityManager;

@Configuration
public class SecurityConfig {

    @Bean(value = "securityManager")
    public MySecurityManager securityManager(){
        return new MySecurityManager();
    }

}
