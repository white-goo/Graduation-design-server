package white.goo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AuthValidators;
import white.goo.dto.R;
import white.goo.entity.User;
import white.goo.serivce.UserService;
import white.goo.util.JwtUtil;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import java.util.HashMap;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));
            UserVO principal = (UserVO) subject.getPrincipal();
            RBucket<Object> bucket = redissonClient.getBucket("user:" + principal.getId());
            bucket.set(principal);
            objectObjectHashMap.put("token", JwtUtil.sign(principal.getId(), JSON.toJSONString(principal.getPermission())));
            return R.ok().put("data",objectObjectHashMap);
        }catch (IncorrectCredentialsException e){
            return R.error().put("msg","密码错误");
        }catch (AuthenticationException e) {
            return R.error().put("msg",e.getMessage());
        }
    }

    @GetMapping("/info")
    public R info(String token){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        String userId = JwtUtil.getUserId(token);
        UserVO userInfo = UserUtil.getUserInfo(userId);
        objectObjectHashMap.put("roles", userInfo.getPermission());
        objectObjectHashMap.put("name", userInfo.getUsername());
        objectObjectHashMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.ok().put("data",objectObjectHashMap);
    }

    @PostMapping("/logout")
    public R logout(String token){
        SecurityUtils.getSubject().logout();
        RBucket<Object> bucket = redissonClient.getBucket("user:" + JwtUtil.getUserId(token));
        bucket.getAndDelete();
        return R.ok();
    }

    @PostMapping("list")
    public R list(){
        return R.ok().saveData(userService.list(new QueryWrapper<User>().select("id","username")));
    }
}
