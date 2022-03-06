package white.goo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AnonValidate;
import white.goo.annonation.AuthValidator;
import white.goo.annonation.AuthValidators;
import white.goo.annonation.ValidateParam;
import white.goo.client.TeacherClient;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.CourseRole;
import white.goo.constant.Operator;
import white.goo.constant.RedisKey;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.dto.R;
import white.goo.entity.Auth;
import white.goo.entity.Role;
import white.goo.entity.User;
import white.goo.api.AuthService;
import white.goo.api.RoleService;
import white.goo.api.UserService;
import white.goo.util.CacheUtil;
import white.goo.util.JwtUtil;
import white.goo.util.UserUtil;
import white.goo.vo.TeacherVO;
import white.goo.vo.UserVO;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TeacherClient teacherClient;

    @PostMapping("/login")
    @AnonValidate
    public R login(@RequestBody User user) {
        HashMap<String, String> map = new HashMap<>(1);
        user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()).eq(User::getIsDelete,false));
        if (Objects.isNull(user)) {
            return R.error("无权访问");
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        if ("admin".equals(user.getUsername())) {
            userVO.setPermission(JSON.toJSONString(Collections.singletonList("admin")));
        } else {
            List<String> list = JSON.parseArray(user.getRoleIds(), String.class);
            List<String> collect = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(list)) {
                List<String> authIds = new ArrayList<>();
                List<Role> roles = roleService.listByIds(list);
                if (CollectionUtil.isNotEmpty(roles)) {
                    for (Role role : roles) {
                        authIds.addAll(JSON.parseArray(role.getAuthId(), String.class));
                    }
                }
                collect = authService.listByIds(authIds).stream().map(Auth::getAuthName).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(collect)) {
                    collect = new ArrayList<>(10);
                }
                TeacherVO teacherVO = new TeacherVO();
                teacherVO.setId(user.getId());
                if (collect.contains(CourseRole.COURSE_TEACHER)) {
                    teacherVO.setName(user.getName());
                    teacherClient.save(teacherVO);
                } else {
                    teacherClient.deleteById(teacherVO);
                }
            }
            userVO.setPermission(JSON.toJSONString(collect));
        }
        userVO.setPassword(null);
        cacheUtil.set(RedisKey.User.getValue() + user.getId(), userVO);
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
        cacheUtil.remove(RedisKey.User.getValue() + JwtUtil.getUserId(token));
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
        User byId = userService.getById(idVO.getId());
        if ("admin".equals(byId.getUsername())) {
            return R.error("系统用户无法删除");
        }
        userService.update(Wrappers.<User>lambdaUpdate().eq(User::getId, idVO.getId()).set(User::getIsDelete, true));
        TeacherVO teacherVO = new TeacherVO();
        teacherVO.setId(idVO.getId());
        teacherClient.deleteById(teacherVO);
        cacheUtil.remove(RedisKey.User.getValue() + idVO.getId());
        return R.ok();
    }

    @AuthValidator(value = "userUpdateValidator")
    @PostMapping("update")
    public R update(@RequestBody UserVO userVO) {
        User user = new User();
        BeanUtil.copyProperties(userVO, user);
        userService.updateById(user);
        cacheUtil.remove(RedisKey.User.getValue() + userVO.getId());
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
    public R load(@RequestBody IdVO idVO) {
        return R.ok().saveData(userService.getOne(Wrappers.<User>lambdaQuery().select(User::getUsername, User::getId, User::getName).eq(User::getId, idVO.getId())));
    }

    @AuthValidator(value = "userUpdateValidator")
    @PostMapping("addCourse")
    public R addCourse(@RequestBody UserVO userVO) {
        User byId = userService.getById(userVO.getId());
        byId.setCourses(JSON.toJSONString(userVO.getCourses()));
        userService.updateById(byId);
        cacheUtil.update(RedisKey.User.getValue() + userVO.getId(), userVO);
        return R.ok();
    }

    @AuthValidators(opt = Operator.OR, value = {
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(AuthRoleConstant.AUTH_ADMIN)
            }),
            @AuthValidator(value = "roleValidator", param = {
                    @ValidateParam(AuthRoleConstant.AUTH_USER_ADMIN)
            })
    })
    @PostMapping("save")
    public R save(@RequestBody UserVO userVO) {
        User user = new User();
        BeanUtil.copyProperties(userVO, user);
        userService.save(user);
        return R.ok();
    }

    @PostMapping("getOne")
    public User get(@RequestBody Wrapper<User> queryWrapper) {
        return userService.getOne(queryWrapper);
    }

    @PostMapping("insert")
    public boolean insert(@RequestBody User entity) {
        return userService.save(entity);
    }

}
