package white.goo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AnonValidate;
import white.goo.dto.R;
import white.goo.entity.User;
import white.goo.serivce.UserService;
import white.goo.util.JwtUtil;
import white.goo.util.UserUtil;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @AnonValidate
    public R login(@RequestBody User user){
        HashMap<String, String> map = new HashMap<>(1);
        user = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()).eq("password",user.getPassword()));
        if(Objects.isNull(user)){
            return R.error("无权访问");
        }
        RBucket<Object> bucket = redissonClient.getBucket("user:" + user.getId());
        bucket.set(user);
        map.put("token", JwtUtil.sign(user.getId(), user.getPermission()));
        return R.ok().put("data",map);
    }

    @GetMapping("/info")
    @AnonValidate
    public R info(String token){
        HashMap<String, Object> map = new HashMap<>(3);
        String userId = JwtUtil.getUserId(token);
        User user = UserUtil.getUserInfo(userId);
        map.put("roles", JSON.parseArray(user.getPermission()));
        map.put("name", user.getUsername());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.ok().put("data",map);
    }

    @PostMapping("/logout")
    public R logout(String token){
        RBucket<Object> bucket = redissonClient.getBucket("user:" + JwtUtil.getUserId(token));
        bucket.getAndDelete();
        return R.ok();
    }

    @PostMapping("list")
    public R list(){
        return R.ok().saveData(userService.list(new QueryWrapper<User>().select("id","username")));
    }
}
