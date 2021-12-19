package white.goo.controller;

import org.springframework.web.bind.annotation.*;
import white.goo.dto.R;
import white.goo.entity.User;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @PostMapping("/login")
    public R login(@RequestBody User user){
        return R.ok().data("token","admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles", "[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }

}
