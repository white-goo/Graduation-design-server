package white.goo.constant;

import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;

@RoleAuth("course")
public class CourseRole {

    /**
     * 课程系统管理员
     */
    @Permission("课程系统管理员")
    public static final String COURSE_SYS_ADMIN = "COURSE:COURSE_SYS_ADMIN";

    /**
     * 教师
     */
    @Permission("教师")
    public static final String COURSE_TEACHER = "COURSE:COURSE_TEACHER";

    /**
     * 学生
     */
    @Permission("学生")
    public static final String COURSE_STUDENT = "COURSE:COURSE_STUDENT";

}
