package white.goo.entity;

import lombok.Data;
import white.goo.entity.conties.CourseFormEnum;
import white.goo.entity.conties.CourseStatusEnum;

@Data
public class Course extends BaseEntity {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 授课老师id
     */
    private String teacherId;

    /**
     * 课程状态
     */
    private CourseStatusEnum status;

    /**
     * 课程学分
     */
    private Integer credit;

    /**
     * 上课地点
     */
    private String place;

    /**
     * 上课形式
     */
    private CourseFormEnum form;

}
