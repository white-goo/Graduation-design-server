package white.goo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import white.goo.annotation.FK;
import white.goo.entity.conties.CourseFormEnum;
import white.goo.entity.conties.CourseStatusEnum;
import white.goo.service.TeacherService;

import java.util.Date;

@Data
public class Course extends BaseEntity {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 授课老师id
     */
    @FK(value = Teacher.class,service = TeacherService.class)
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

    /**
     * 开课时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

}
