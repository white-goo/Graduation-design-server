package white.goo.constant;

import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;

@RoleAuth
public class CourseRole {

    @Permission("课程系统管理员")
    public static final String COURSE_SYS_ADMIN = "COURSE:COURSE_SYS_ADMIN";
    @Permission("教师")
    public static final String COURSE_TEACHER = "COURSE:COURSE_TEACHER";

}
