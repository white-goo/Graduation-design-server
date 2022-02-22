package white.goo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AnonValidate;
import white.goo.annonation.AuthValidator;
import white.goo.annonation.AuthValidators;
import white.goo.annonation.ValidateParam;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.Operator;
import white.goo.constant.RedisKey;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.dto.R;
import white.goo.entity.Auth;
import white.goo.entity.Role;
import white.goo.entity.User;
import white.goo.serivce.AuthService;
import white.goo.serivce.RoleService;
import white.goo.serivce.UserService;
import white.goo.util.JwtUtil;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @AnonValidate
    public R login(@RequestBody User user) {
        HashMap<String, String> map = new HashMap<>(1);
        user = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()).eq("password", user.getPassword()));
        if (Objects.isNull(user)) {
            return R.error("无权访问");
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        if ("1".equals(user.getId())) {
            userVO.setPermission(JSON.toJSONString(Collections.singletonList("admin")));
        } else {
            List<String> list = JSON.parseArray(user.getRoleIds(), String.class);
            List<String> authIds = new ArrayList<>();
            for (Role role : roleService.listByIds(list)) {
                authIds.addAll(JSON.parseArray(role.getAuthId(), String.class));
            }
            List<String> collect = authService.listByIds(authIds).stream().map(Auth::getAuthName).collect(Collectors.toList());
            userVO.setPermission(JSON.toJSONString(collect));
        }

        RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + user.getId());
        bucket.set(userVO);
        map.put("token", JwtUtil.sign(user.getId(), userVO.getPermission()));
        return R.ok().put("data", map);
    }

    @GetMapping("/info")
    @AnonValidate
    public R info(String token) {
        HashMap<String, Object> map = new HashMap<>(3);
        String userId = JwtUtil.getUserId(token);
        UserVO userVO = UserUtil.getUserInfo(userId);
        map.put("roles", JSON.parseArray(userVO.getPermission()));
        map.put("name", userVO.getUsername());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.ok().put("data", map);
    }

    @PostMapping("/logout")
    public R logout(String token) {
        RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + JwtUtil.getUserId(token));
        bucket.getAndDelete();
        return R.ok();
    }

    @PostMapping("list")
    public R list(@RequestBody(required = false) Query<User> page) {
        return R.ok().saveData(userService.listVO(page));
    }

    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(value = AuthRoleConstant.AUTH_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(value = AuthRoleConstant.AUTH_USER_ADMIN)
            })
    })
    @PostMapping("delete")
    public R delete(@RequestBody IdVO idVO) {
        if("1".equals(idVO.getId())){
            return R.error("系统用户无法删除");
        }
        userService.removeById(idVO.getId());
        RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + idVO.getId());
        bucket.getAndDelete();
        return R.ok();
    }

    @AuthValidator(value = "userUpdateValidator")
    @PostMapping("update")
    public R update(@RequestBody UserVO userVO){
        User user = new User();
        BeanUtil.copyProperties(userVO, user);
        userService.updateById(user);
        RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + userVO.getId());
        bucket.getAndDelete();
        return R.ok();
    }

    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(value = AuthRoleConstant.AUTH_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(value = AuthRoleConstant.AUTH_USER_ADMIN)
            }),
            @AuthValidator(value = "userUpdateValidator")
    })
    @PostMapping("load")
    public R load(@RequestBody IdVO idVO){
        return R.ok().saveData(userService.getOne(Wrappers.<User>lambdaQuery().select(User::getUsername,User::getId).eq(User::getId, idVO.getId())));
    }
}
