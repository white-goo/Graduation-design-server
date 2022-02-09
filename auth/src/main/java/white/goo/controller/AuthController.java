package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.dto.R;
import white.goo.serivce.AuthService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/getAuthRoleInfo")
    public R getAuthRoleInfo(){
        List<Map<String,Object>> info =  authService.getAuthRoleInfo();

        return R.ok();
    }

}
