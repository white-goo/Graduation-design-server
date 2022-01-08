package white.goo.controller;

import org.springframework.web.bind.annotation.*;
import white.goo.dto.R;
import white.goo.entity.User;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @PostMapping("/login")
    public R login(@RequestBody User user){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("token", "admin");
        return R.ok().put("data",objectObjectHashMap);
    }

    @GetMapping("/info")
    public R info(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("roles", "[admin]");
        objectObjectHashMap.put("name", "admin");
        objectObjectHashMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.ok().put("data",objectObjectHashMap);
    }

    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }

}
