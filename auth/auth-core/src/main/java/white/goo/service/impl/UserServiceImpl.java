package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.constant.RedisKey;
import white.goo.dto.Query;
import white.goo.entity.User;
import white.goo.repository.UserMapper;
import white.goo.api.UserService;
import white.goo.util.CacheUtil;
import white.goo.util.DBUtil;
import white.goo.vo.UserVO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/user")
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public List<UserVO> listVO(Query<User> page) {
        return super.page(page, DBUtil.buildCondition(page).select("id", "username", "name", "create_time", "edit_time").ne("username", "admin").eq("is_delete",false)).getRecords().stream().map(item -> {
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(item, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void addCourse(UserVO userVO) {
        User byId = getById(userVO.getId());
        byId.setCourses(JSON.toJSONString(userVO.getCourses()));
        updateById(byId);
        cacheUtil.update(RedisKey.User + userVO.getId(), userVO);
    }

}
