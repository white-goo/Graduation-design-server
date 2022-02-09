package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.Permission;
import white.goo.annonation.ValidatePermission;
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
    @ValidatePermission("admin")
    public R list(@RequestBody Query<Course> page){
        return R.ok().put("courseList",courseService.listVO(page));
    }

    @PostMapping("/load")
    public R load(@RequestBody IdVO id){
        return R.ok().put("course",courseService.loadById(id.getId()));
    }

    @PostMapping("/save")
    public R save(@RequestBody CourseVO courseVO){
        boolean save = courseService.save(courseVO);
        if(save){
            return R.ok();
        }else {
            return R.error("保存失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody CourseVO courseVO){
        Boolean update = courseService.update(courseVO);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @PostMapping("/delete")
    public R delete(@RequestBody IdVO idVO){
        courseService.delete(idVO);
        return R.ok();
    }

    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<IdVO> idVOList) {
        courseService.deleteBatch(idVOList);
        return R.ok();
    }

}
