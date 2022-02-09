package white.goo.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;
import white.goo.entity.Auth;
import white.goo.serivce.AuthService;
import white.goo.util.SpringUtil;
import white.goo.util.UserUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class InitPermission implements ApplicationRunner {

    @Value("${sys-module-name}")
    private String moduleName;

    @Autowired
    private AuthService authService;

    @Override
    @Transactional
    public void run(ApplicationArguments args){

        Map<String, Object> beansWithAnnotation = SpringUtil.getApplicationContext().getBeansWithAnnotation(RoleAuth.class);
        List<Auth> auths = authService.list(new QueryWrapper<Auth>().eq("module_name", moduleName));
        beansWithAnnotation.forEach((k, v) -> {
            auths.stream().filter(item->{
                for (Field declaredField : v.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    try {
                        if(Objects.equals(item.getAuthName(),declaredField.get(v))){
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }).forEach(item->authService.remove(new QueryWrapper<Auth>().eq("id", item.getId())));

            for (Field declaredField : v.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                Permission permission = declaredField.getAnnotation(Permission.class);
                if(Objects.nonNull(permission)){
                    try {
                        String permissionValue = (String) declaredField.get(v);
                        Auth one = authService.getOne(new QueryWrapper<Auth>().eq("module_name", moduleName).eq("auth_name", permission.value()));
                        if(Objects.isNull(one)){
                            Auth auth = new Auth();
                            auth.setAuthName(permissionValue);
                            auth.setAuthShowName(permission.value());
                            auth.setModuleName(moduleName);
                            authService.save(auth);
                        }else {
                            one.setAuthName(permissionValue);
                            one.setAuthShowName(permission.value());
                            authService.saveOrUpdate(one);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
