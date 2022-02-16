package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AuthValidator;
import white.goo.annonation.AuthValidators;
import white.goo.annonation.ValidateParam;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.Operator;
import white.goo.dto.R;
import white.goo.serivce.AuthService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/list")
    @AuthValidators(opt = Operator.OR,value = {
            @AuthValidator(value = "roleValidator",param = {
                    @ValidateParam(AuthRoleConstant.AUTH_ADMIN)
            }),
            @AuthValidator(value = "roleValidator",param = {
                    @ValidateParam(AuthRoleConstant.AUTH_ROLE_ADMIN)
            }),
            @AuthValidator(value = "roleValidator",param = {
                    @ValidateParam(AuthRoleConstant.AUTH_USER_ADMIN)
            })
    })
    public R list(){
        return R.ok().saveData(authService.list());
    }

}
