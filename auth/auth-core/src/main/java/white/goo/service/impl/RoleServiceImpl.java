package white.goo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.constant.RedisKey;
import white.goo.dto.Query;
import white.goo.entity.Role;
import white.goo.entity.User;
import white.goo.repository.RoleMapper;
import white.goo.api.RoleService;
import white.goo.api.UserService;
import white.goo.util.DBUtil;
import white.goo.vo.RoleVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/role")
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<RoleVO> listVO(Query<Role> page) {
        return super.page(page, DBUtil.buildCondition(page)).getRecords().stream().map(item -> {
            RoleVO roleVo = new RoleVO();
            BeanUtil.copyProperties(item, roleVo,"userId");
            roleVo.setUserId(JSON.parseArray(item.getUserId(),String.class));
            return roleVo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(RoleVO roleVO) {
        Role role = new Role();
        BeanUtil.copyProperties(roleVO,role);
        role.setAuthId(roleVO.getAuthId().toString());
        role.setUserId(roleVO.getUserId().toString());
        save(role);
        roleVO.getUserId().forEach(item->{
            User user = userService.getById(item);
            List<String> list = JSON.parseArray(user.getRoleIds(), String.class);
            if(Objects.isNull(list)){
                list = new ArrayList<>(10);
            }
            list.add(role.getId());
            user.setRoleIds(JSON.toJSONString(list));
            userService.updateById(user);
        });
    }

    @Override
    public RoleVO load(String id) {
        Role byId = getById(id);
        RoleVO roleVO = new RoleVO();
        BeanUtil.copyProperties(byId,roleVO,"userId");
        List<String> authId = JSON.parseArray(byId.getAuthId(), String.class);
        List<String> userId = JSON.parseArray(byId.getUserId(), String.class);
        roleVO.setAuthId(authId);
        roleVO.setUserId(userId);
        return roleVO;
    }

    @Override
    @Transactional
    public void update(RoleVO roleVO) {
        Role role = getById(roleVO.getId());
        List<String> oldUserIds = JSON.parseArray(role.getUserId(), String.class);
        List<String> newUserIds = roleVO.getUserId();
        oldUserIds.stream().filter(item->!newUserIds.contains(item)).forEach(item->{
            User user = userService.getById(item);
            if(Objects.nonNull(user)){
                List<String> list = JSON.parseArray(user.getRoleIds(), String.class);
                list.remove(role.getId());
                user.setRoleIds(JSON.toJSONString(list));
                userService.updateById(user);
                RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + user.getId());
                bucket.getAndDelete();
            }
        });
        newUserIds.stream().filter(item->!oldUserIds.contains(item)).forEach(item->{
            User user = userService.getById(item);
            List<String> list = JSON.parseArray(user.getRoleIds(), String.class);
            if(CollectionUtil.isEmpty(list)){
                list = new ArrayList<>(10);
            }
            list.add(role.getId());
            user.setRoleIds(JSON.toJSONString(list));
            userService.updateById(user);
            RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + user.getId());
            bucket.getAndDelete();
        });
        role.setUserId(JSON.toJSONString(newUserIds));
        role.setRoleName(roleVO.getRoleName());
        role.setAuthId(JSON.toJSONString(roleVO.getAuthId()));
        this.updateById(role);
    }

}
