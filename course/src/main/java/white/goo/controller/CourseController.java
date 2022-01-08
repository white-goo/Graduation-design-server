package white.goo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.dto.IdVO;
import white.goo.dto.R;
import white.goo.entity.Course;
import white.goo.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/list")
    public R list(Page<Course> page){
        return R.ok().put("courseList",courseService.listVO(page));
    }

    @PostMapping("/load")
    public R load(@RequestBody IdVO id){
        return R.ok().put("course",courseService.loadById(id.getId()));
    }

}
