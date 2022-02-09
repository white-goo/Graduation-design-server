package white.goo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.dto.R;
import white.goo.entity.User;
import white.goo.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/login")
    public R login(@RequestBody User user, HttpServletResponse response){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));
            User principal = (User) subject.getPrincipal();
            Cookie token = new Cookie("Token", JwtUtil.sign(principal.getId(),principal.getPermission()));
            token.setDomain("localhost");
            token.setPath("/");
            token.setHttpOnly(true);
            response.addCookie(token);
            RBucket<Object> bucket = redissonClient.getBucket("user:" + principal.getId());
            bucket.set(principal);
            objectObjectHashMap.put("token", "admin");
            return R.ok().put("data",objectObjectHashMap);
        }catch (IncorrectCredentialsException e){
            return R.error().put("msg","密码错误");
        }catch (AuthenticationException e) {
            return R.error().put("msg",e.getMessage());
        }
    }

    @GetMapping("/info")
    public R info(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("roles", new ArrayList<String>(){{
            add("app");
        }});
        objectObjectHashMap.put("name", "admin");
        objectObjectHashMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.ok().put("data",objectObjectHashMap);
    }

    @PostMapping("/logout")
    public R logout(HttpServletResponse response){
        SecurityUtils.getSubject().logout();
        Cookie token = new Cookie("Token", null);
        token.setDomain("localhost");
        token.setMaxAge(0);
        token.setPath("/");
        token.setHttpOnly(true);
        response.addCookie(token);
        return R.ok();
    }

}
