package white.goo.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.goo.entity.Auth;
import white.goo.repository.AuthMapper;
import white.goo.serivce.AuthService;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {
    @Override
    public List<Map<String, Object>> getAuthRoleInfo() {

        List<Auth> auths = baseMapper.selectList(new QueryWrapper<Auth>());

        return null;
    }
}
