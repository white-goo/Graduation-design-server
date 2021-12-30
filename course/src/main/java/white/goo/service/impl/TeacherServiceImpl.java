package white.goo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.goo.entity.Teacher;
import white.goo.repository.TeacherMapper;
import white.goo.service.TeacherService;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
}
