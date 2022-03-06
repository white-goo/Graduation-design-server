package white.goo.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import white.goo.util.JwtUtil;

@Component
public class FeignBeforeExecInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("backGroundToken", JwtUtil.signBackGroundToken());
    }
}
