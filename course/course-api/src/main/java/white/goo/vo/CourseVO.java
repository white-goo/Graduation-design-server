package white.goo.vo;

import lombok.Data;
import white.goo.constant.CourseFormEnum;
import white.goo.constant.CourseStatusEnum;
import white.goo.dto.BaseVO;
import white.goo.entity.Teacher;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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

    /**
     * 结束时间
     */
    @NotNull
    private Date endDate;

    /**
     * 课程数量
     */
    private Integer courseAmount;

    /**
     * 该课程的学生
     */
    private List<String> students;

    /**
     * 是否可以抢课
     */
    private Boolean isChoose = false;

}
