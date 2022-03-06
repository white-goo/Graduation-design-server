package white.goo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.AnonValidate;
import white.goo.annonation.AuthValidator;
import white.goo.annonation.AuthValidators;
import white.goo.annonation.ValidateParam;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.Operator;
import white.goo.core.MySecurityManager;
import white.goo.dto.R;
import white.goo.api.AuthService;
import white.goo.entity.Auth;
import white.goo.util.SecurityUtil;
import white.goo.vo.AuthCheckVO;

import java.io.Serializable;
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
        MySecurityManager securityManager = SecurityUtil.getSecurityManager();
        Map<String, Boolean> collect = list.stream().collect(Collectors.toMap(AuthCheckVO::getKey, securityManager::authCheck));
        return R.ok().saveData(collect);
    }

    @PostMapping("remove")
    public boolean remove(@RequestBody String id){
        return authService.removeById(id);
    }

    @PostMapping("save")
    public boolean insert(@RequestBody Auth entity){
        return authService.save(entity);
    }

    @PostMapping("updateById")
    public boolean update(@RequestBody Auth entity){
        return authService.updateById(entity);
    }

    @PostMapping("listByModuleName")
    public R listByModuleName(@RequestBody String moduleName){
        return R.ok().saveData(authService.list(new QueryWrapper<Auth>().eq("module_name",moduleName)));
    }

    @PostMapping("getByAuthName")
    public Auth getByAuthName(@RequestBody String name){
        return authService.getOne(new QueryWrapper<Auth>().eq("auth_name", name));
    }

}
