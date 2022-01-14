package white.goo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.goo.dto.IdVO;
import white.goo.entity.Teacher;
import white.goo.repository.TeacherMapper;
import white.goo.service.TeacherService;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public int delete(IdVO of) {
        return baseMapper.delete(new QueryWrapper<Teacher>().eq("id", of.getId()));
    }

    @Override
    public int deleteBatch(List<String> teacherIds) {
        return baseMapper.deleteBatchIds(teacherIds);
    }
}
