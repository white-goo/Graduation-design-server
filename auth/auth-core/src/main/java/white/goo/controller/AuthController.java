package white.goo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import white.goo.annonation.*;
import white.goo.api.Extension;
import white.goo.api.Test2Api;
import white.goo.api.TestApi;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.Operator;
import white.goo.dto.IdVO;
import white.goo.dto.R;
import white.goo.api.AuthService;
import white.goo.entity.Auth;
import white.goo.extension.Test2Point;
import white.goo.extension.TestPoint;
import white.goo.util.ExtendUtil;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/list")
    @CompositeValidators(opt = Operator.OR, value = {
            @AuthValidators(opt = Operator.OR,value = {
                    @AuthValidator(value = "roleValidator",param = {
                            @ValidateParam(AuthRoleConstant.AUTH_ADMIN)
                    }),
                    @AuthValidator(value = "roleValidator",param = {
                            @ValidateParam(AuthRoleConstant.AUTH_ROLE_ADMIN)
                    })
            }),
            @AuthValidators(opt = Operator.AND, value = {
                    @AuthValidator(value = "roleValidator",param = {
                            @ValidateParam(AuthRoleConstant.AUTH_USER_ADMIN)
                    })
            })
    })
    public R list(){
        return R.ok().saveData(authService.list());
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

    @PostMapping("test")
    @AnonValidate
    public void Test(){

        List<Extension> point = ExtendUtil.getPoint(TestPoint.class);
        for (Extension extension : point) {
            TestApi testApi = extension.getProvider();
            List<String> test = testApi.test(IdVO.of("123"));
            String s1 = testApi.test3();
            System.out.println();
        }

        List<Extension> point1 = ExtendUtil.getPoint(Test2Point.class);
        for (Extension extension : point1) {
            Test2Api test2Api = extension.getProvider();
            String test = test2Api.test22();
            System.out.println();
        }
    }

}
