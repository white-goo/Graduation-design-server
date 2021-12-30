package white.goo.vo;

import lombok.Data;
import white.goo.dto.BaseVO;
import white.goo.entity.Teacher;
import white.goo.entity.conties.CourseFormEnum;
import white.goo.entity.conties.CourseStatusEnum;

@Data
public class CourseVO extends BaseVO {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 授课老师id
     */
    private Teacher teacher;

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
