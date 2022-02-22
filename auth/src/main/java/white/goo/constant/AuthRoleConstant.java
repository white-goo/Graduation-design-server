package white.goo.constant;

import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;

@RoleAuth("auth")
public class AuthRoleConstant {

    @Permission("权限管理员")
    public static final String AUTH_ADMIN = "auth:AUTH_ADMIN";

    @Permission("角色管理员")
    public static final String AUTH_ROLE_ADMIN = "auth:AUTH_ROLE_ADMIN";

    @Permission("权限模块默认角色")
    public static final String AUTH_DEFAULT = "auth:AUTH_DEFAULT";

    @Permission("用户管理员")
    public static final String AUTH_USER_ADMIN = "auth:AUTH_USER_ADMIN";
}
