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
import white.goo.util.SecurityUtil;
import white.goo.util.SpringUtil;
import white.goo.vo.AuthCheckVO;
import white.goo.core.SecurityManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/authCheck")
    public R authCheck(@RequestBody List<AuthCheckVO> list){
        SecurityManager securityManager = SecurityUtil.getSecurityManager();
        Map<String, Boolean> collect = list.stream().collect(Collectors.toMap(AuthCheckVO::getKey, securityManager::authCheck));
        return R.ok().saveData(collect);
    }

}
