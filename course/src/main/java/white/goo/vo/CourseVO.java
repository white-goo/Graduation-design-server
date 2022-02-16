package white.goo.vo;

import lombok.Data;
import white.goo.dto.BaseVO;
import white.goo.entity.Teacher;
import white.goo.entity.conties.CourseFormEnum;
import white.goo.entity.conties.CourseStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CourseVO extends BaseVO {

    /**
     * 课程名称
     */
    @NotNull
    private String courseName;

    /**
     * 授课老师id
     */
    @NotNull
    private Teacher teacher;

    /**
     * 课程状态
     */
    private CourseStatusEnum status;

    /**
     * 课程学分
     */
    @NotNull
    private Integer credit;

    /**
     * 上课地点
     */
    @NotNull
    private String place;

    /**
     * 上课形式
     */
    @NotNull
    private CourseFormEnum form;

    /**
     * 开课时间
     */
    @NotNull
    private Date date;

}
