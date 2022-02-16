package white.goo.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.goo.entity.Auth;
import white.goo.repository.AuthMapper;
import white.goo.serivce.AuthService;


@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {
}
