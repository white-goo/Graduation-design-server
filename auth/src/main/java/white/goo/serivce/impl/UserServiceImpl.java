package white.goo.serivce.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.goo.dto.Query;
import white.goo.entity.User;
import white.goo.repository.UserMapper;
import white.goo.serivce.UserService;
import white.goo.util.DBUtil;
import white.goo.vo.UserVO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public List<UserVO> listVO(Query<User> page) {
        return super.page(page, DBUtil.buildCondition(page).select("id","username","create_time","edit_time").ne("id",1)).getRecords().stream().map(item -> {
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(item, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }
}
