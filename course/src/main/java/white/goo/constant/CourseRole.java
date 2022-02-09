package white.goo.constant;

import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;

@RoleAuth
public class CourseRole {

    @Permission("课程系统管理员")
    public static final String COURSE_SYS_ADMIN = "COURSE_SYS_ADMIN";

}
