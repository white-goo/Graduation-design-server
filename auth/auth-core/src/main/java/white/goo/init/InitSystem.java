package white.goo.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import white.goo.api.UserService;
import white.goo.entity.User;

import java.util.Objects;

@Component
public class InitSystem implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) {

        User admin = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, "admin"));
        if(Objects.isNull(admin)){
            admin = new User();
            admin.setPassword("admin");
            admin.setUsername("admin");
            admin.setName("管理员");
            admin.setIsDelete(false);
            userService.save(admin);
        }
    }
}
