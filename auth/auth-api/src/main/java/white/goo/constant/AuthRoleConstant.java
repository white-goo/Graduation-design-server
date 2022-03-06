package white.goo.constant;

import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;

@RoleAuth("auth")
public class AuthRoleConstant {

    /**
     * 权限管理员
     */
    @Permission("权限管理员")
    public static final String AUTH_ADMIN = "auth:AUTH_ADMIN";

    /**
     * 角色管理员
     */
    @Permission("角色管理员")
    public static final String AUTH_ROLE_ADMIN = "auth:AUTH_ROLE_ADMIN";

    /**
     * 权限模块默认角色
     */
    @Permission("权限模块默认角色")
    public static final String AUTH_DEFAULT = "auth:AUTH_DEFAULT";

    /**
     * 用户管理员
     */
    @Permission("用户管理员")
    public static final String AUTH_USER_ADMIN = "auth:AUTH_USER_ADMIN";
}
