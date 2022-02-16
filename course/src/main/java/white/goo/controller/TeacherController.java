package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.dto.R;
import white.goo.service.TeacherService;

@RestController
@RequestMapping("/course/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/list")
    public R list(){
        return R.ok().saveData(teacherService.list());
    }

}
