package white.goo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.dto.IdVO;
import white.goo.entity.Teacher;

import java.util.List;

public interface TeacherService extends IService<Teacher> {
    int delete(IdVO of);

    int deleteBatch(List<String> teacherIds);
}
