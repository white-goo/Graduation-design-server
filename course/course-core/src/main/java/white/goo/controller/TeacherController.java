package white.goo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.constant.CourseRole;
import white.goo.dto.R;
import white.goo.entity.Teacher;
import white.goo.service.TeacherService;
import white.goo.vo.TeacherVO;

import java.util.Objects;

@RestController
@RequestMapping("/course/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/list")
    public R list(){
        return R.ok().saveData(teacherService.list(Wrappers.<Teacher>lambdaQuery().eq(Teacher::getIsDelete,false)));
    }

    @PostMapping("save")
    public R save(@RequestBody TeacherVO teacherVO){
        Teacher teacher = new Teacher();
        BeanUtil.copyProperties(teacherVO, teacher);
        Teacher byId = teacherService.getById(teacherVO.getId());
        if(Objects.nonNull(byId)){
            return R.ok().saveData(teacherService.updateById(teacher));
        }
        return R.ok().saveData(teacherService.save(teacher));
    }

    @PostMapping("deleteById")
    public R deleteById(@RequestBody TeacherVO teacherVO){
        return R.ok().saveData(teacherService.update(Wrappers.<Teacher>lambdaUpdate().eq(Teacher::getId, teacherVO.getId()).set(Teacher::getIsDelete, true)));
    }

}
