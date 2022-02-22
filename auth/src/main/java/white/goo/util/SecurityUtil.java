package white.goo.util;

import white.goo.core.SecurityManager;

public class SecurityUtil {
    public static SecurityManager getSecurityManager(){
        return (SecurityManager) SpringUtil.getBean("securityManager");
    }
}
