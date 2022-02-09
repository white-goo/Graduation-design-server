package white.goo.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import white.goo.entity.User;
import white.goo.serivce.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class InitSystem implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) {

        User id = userService.getOne(new QueryWrapper<User>().eq("id", 1));
        if(Objects.isNull(id)){
            id = new User();
            id.setId(1L);
            id.setPassword("admin");
            id.setUsername("admin");
            List<String> list = Arrays.asList("admin");
            id.setPermission(JSON.toJSONString(list));
            userService.save(id);
        }
    }
}
