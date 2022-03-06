package white.goo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.dto.R;
import white.goo.vo.TeacherVO;

@FeignClient(value = "course",path = "/course/teacher")
public interface TeacherClient {

    @PostMapping("save")
    R save(@RequestBody TeacherVO teacherVO);

    @PostMapping("deleteById")
    R deleteById(@RequestBody TeacherVO teacherVO);

}
