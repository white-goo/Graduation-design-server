package white.goo.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import white.goo.dto.IdVO;

import java.util.List;

/**
 * @author shiyk
 * @date 2022/5/3
 */
public interface TestApi {

    @PostMapping("test")
    List<String> test(@RequestBody IdVO idVO);

    @PostMapping("test3")
    String test3();

}
