package white.goo.serivce.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import white.goo.dto.Query;
import white.goo.entity.Auth;
import white.goo.entity.Role;
import white.goo.entity.User;
import white.goo.repository.RoleMapper;
import white.goo.serivce.AuthService;
import white.goo.serivce.RoleService;
import white.goo.serivce.UserService;
import white.goo.util.DBUtil;
import white.goo.vo.RoleVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

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
            List<String> collect = authService.listByIds(roleVO.getAuthId()).stream().map(Auth::getAuthName).collect(Collectors.toList());
            User user = userService.getById(item);
            List<String> list = JSON.parseArray(user.getPermission(), String.class);
            list.addAll(collect);
            collect = list.stream().distinct().collect(Collectors.toList());
            user.setPermission(JSON.toJSONString(collect));
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

}
