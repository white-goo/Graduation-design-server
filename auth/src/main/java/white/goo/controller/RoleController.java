package white.goo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import white.goo.annonation.AuthValidator;
import white.goo.annonation.AuthValidators;
import white.goo.annonation.ValidateParam;
import white.goo.constant.AuthRoleConstant;
import white.goo.constant.Operator;
import white.goo.dto.IdVO;
import white.goo.dto.Query;
import white.goo.dto.R;
import white.goo.entity.Role;
import white.goo.serivce.RoleService;
import white.goo.vo.RoleVO;

@RestController
@RequestMapping("/auth/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("list")
    @AuthValidators(opt = Operator.OR,value = {
            @AuthValidator(value = "roleValidator",param = {
                    @ValidateParam(AuthRoleConstant.AUTH_ADMIN)
            }),
            @AuthValidator(value = "roleValidator",param = {
                    @ValidateParam(AuthRoleConstant.AUTH_ROLE_ADMIN)
            })
    })
    public R list(@RequestBody Query<Role> page){
        return R.ok().saveData(roleService.listVO(page));
    }

    @PostMapping("load")
    public R load(@RequestBody IdVO idVO){
        return R.ok().saveData(roleService.load(idVO.getId()));
    }

    @PostMapping("save")
    public R save(@RequestBody RoleVO roleVO){
        roleService.save(roleVO);
        return R.ok();
    }

    @PostMapping("delete")
    public R delete(@RequestBody IdVO idVO){
        roleService.removeById(idVO.getId());
        return R.ok();
    }

    @PostMapping("update")
    public R update(@RequestBody RoleVO roleVO){
        roleService.update(roleVO);
        return R.ok();
    }
}
