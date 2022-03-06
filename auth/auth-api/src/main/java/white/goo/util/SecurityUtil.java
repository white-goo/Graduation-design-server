package white.goo.util;

import white.goo.core.MySecurityManager;

public class SecurityUtil {
    public static MySecurityManager getSecurityManager(){
        return (MySecurityManager) SpringUtil.getBean("securityManager");
    }
}
