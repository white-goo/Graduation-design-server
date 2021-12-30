package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.dto.R;
import white.goo.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/list")
    public R list(){
        return R.ok().data(courseService.listVO());
    }

    @PostMapping("/load")
    public R load(@RequestBody String id){
        return R.ok().data(courseService.loadById(id));
    }

}
