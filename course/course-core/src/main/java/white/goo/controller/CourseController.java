package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.*;
import white.goo.constant.CourseRole;
import white.goo.constant.Operator;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.dto.R;
import white.goo.entity.Course;
import white.goo.service.CourseService;
import white.goo.vo.CourseVO;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/list")
    public R list(@RequestBody Query<Course> page) {
        return R.ok().put("courseList", courseService.listVO(page));
    }

    @PostMapping("/load")
    public R load(@RequestBody IdVO id) {
        return R.ok().put("course", courseService.loadById(id.getId()));
    }

    @PostMapping("/save")
    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_SYS_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_TEACHER)
            })
    })
    public R save(@RequestBody CourseVO courseVO) {
        boolean save = courseService.save(courseVO);
        if (save) {
            return R.ok();
        } else {
            return R.error("保存失败");
        }
    }

    @PostMapping("/update")
    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_SYS_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_TEACHER)
            })
    })
    public R update(@RequestBody CourseVO courseVO) {
        Boolean update = courseService.update(courseVO);
        if (update) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @PostMapping("/delete")
    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_SYS_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_TEACHER)
            })
    })
    public R delete(@RequestBody IdVO idVO) {
        courseService.delete(idVO);
        return R.ok();
    }

    @PostMapping("/deleteBatch")
    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_SYS_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_TEACHER)
            })
    })
    public R deleteBatch(@RequestBody List<IdVO> idVOList) {
        courseService.deleteBatch(idVOList);
        return R.ok();
    }

    @PostMapping("chooseCourse")
    @AuthValidator(value = "roleValidator", param = {
            @ValidateParam(CourseRole.COURSE_STUDENT)
    })
    @AuthValidators({
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(CourseRole.COURSE_STUDENT)
            }),
            @AuthValidator(value = "isChooseValidator")
    })
    public R chooseCourse(@RequestBody IdVO idVO){
        boolean b = courseService.chooseCourse(idVO);
        return R.ok().saveData(b);
    }

    @PostMapping("listMyCourse")
    @AuthValidator(value = "roleValidator", param = {
            @ValidateParam(CourseRole.COURSE_STUDENT)
    })
    public R listMyCourse(@RequestBody Query<Course> page){
        List<CourseVO> res = courseService.listMyCourse(page);
        return R.ok().saveData(res);
    }

    @PostMapping("deleteMyCourse")
    @AuthValidator(value = "roleValidator", param = {
            @ValidateParam(CourseRole.COURSE_STUDENT)
    })
    public R deleteMyCourse(@RequestBody IdVO idVO){
        courseService.deleteMyCourse(idVO);
        return R.ok();
    }
}
