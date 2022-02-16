package white.goo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import white.goo.core.SecurityManager;

@Configuration
public class SecurityConfig {

    @Bean(value = "securityManager")
    public SecurityManager securityManager(){
        return new SecurityManager();
    }

}
