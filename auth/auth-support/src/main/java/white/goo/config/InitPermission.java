package white.goo.config;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import white.goo.annonation.Permission;
import white.goo.annonation.RoleAuth;
import white.goo.client.AuthClient;
import white.goo.dto.R;
import white.goo.entity.Auth;
import white.goo.util.SpringUtil;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class InitPermission implements ApplicationRunner {

    @Autowired
    private AuthClient authClient;

    @Value("${spring.application.name}")
    private String moduleName;

    @Override
    @Transactional
    public void run(ApplicationArguments args){

        Map<String, Object> beansWithAnnotation = SpringUtil.getApplicationContext().getBeansWithAnnotation(RoleAuth.class);
        R r = null;
        while (Objects.isNull(r)){
            try {
                r = authClient.listByModuleName(moduleName);
            } catch (Exception ignored) {
            }
        }
        List<Auth> oldAuths = r.getData(new TypeReference<List<Auth>>(){});
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
                authClient.remove(item.getId());
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
                        Auth one = authClient.getByAuthName(permissionValue);
                        if (Objects.isNull(one)) {
                            Auth auth = new Auth();
                            auth.setModuleName(moduleName);
                            auth.setAuthName(permissionValue);
                            auth.setAuthShowName(permission.value());
                            authClient.insert(auth);
                        } else {
                            one.setAuthName(permissionValue);
                            one.setAuthShowName(permission.value());
                            authClient.update(one);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
