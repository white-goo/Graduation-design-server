package white.goo.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;
import white.goo.api.AuthService;
import white.goo.constant.RedisKey;
import white.goo.entity.Auth;
import white.goo.util.SpringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class InitPermission implements ApplicationRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${spring.application.name}")
    private String moduleName;

    @Override
    @Transactional
    public void run(ApplicationArguments args){

        Map<String, Object> beansWithAnnotation = SpringUtil.getApplicationContext().getBeansWithAnnotation(RoleAuth.class);
        List<Auth> oldAuths = authService.list(new QueryWrapper<Auth>().eq("module_name",moduleName));
        List<String> newAuths = new ArrayList<>();
        beansWithAnnotation.forEach((k, v) -> {
            for (Field declaredField : v.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                try {
                    newAuths.add((String) declaredField.get(v));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        for (Auth item : oldAuths) {
            if (!newAuths.contains(item.getAuthName())) {
                authService.removeById(item.getId());
            }
        }
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Object v = entry.getValue();
            String value = v.getClass().getAnnotation(RoleAuth.class).value();
            if (!Objects.equals(moduleName, value)) {
                continue;
            }
            for (Field declaredField : v.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                Permission permission = declaredField.getAnnotation(Permission.class);
                if (Objects.nonNull(permission)) {
                    try {
                        String permissionValue = (String) declaredField.get(v);
                        Auth one = authService.getOne(new QueryWrapper<Auth>().eq("auth_name", permissionValue));
                        if (Objects.isNull(one)) {
                            Auth auth = new Auth();
                            auth.setModuleName(moduleName);
                            auth.setAuthName(permissionValue);
                            auth.setAuthShowName(permission.value());
                            authService.save(auth);
                        } else {
                            one.setAuthName(permissionValue);
                            one.setAuthShowName(permission.value());
                            authService.updateById(one);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
