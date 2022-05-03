package white.goo.api;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author shiyk
 * @date 2022/5/3
 */
public interface Test2Api {

    @PostMapping("test22")
    String test22();

}
