package white.goo.controller;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import white.goo.annonation.AnonValidate;
import white.goo.core.MySecurityManager;
import white.goo.dto.R;
import white.goo.util.SecurityUtil;
import white.goo.vo.AuthCheckVO;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shiyk
 * @date 2022/3/8
 */
@RestController
public class AuthCheckController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String module;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/auth/authCheck")
    public R authCheck(@RequestBody List<AuthCheckVO> list){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", request.getHeader("Cookie"));
        headers.set("User-Agent", request.getHeader("User-Agent"));
        MySecurityManager securityManager = SecurityUtil.getSecurityManager();
        Map<String, Boolean> collect = list.stream().collect(Collectors.toMap(AuthCheckVO::getKey, item->{
            String module = item.getModule();
            if(this.module.equals(module)){
                return securityManager.authCheck(item);
            }else {
                List<ServiceInstance> course = discoveryClient.getInstances(module);
                ServiceInstance serviceInstance = course.get(discoveryClient.getOrder());
                URI uri = serviceInstance.getUri();
                List<AuthCheckVO> authCheckVOS = new ArrayList<>(1);
                authCheckVOS.add(item);
                ResponseEntity<R> rResponseEntity = restTemplate.exchange(uri + "/auth/authCheck", HttpMethod.POST, new HttpEntity<>(authCheckVOS, headers), R.class);
                if(200 == rResponseEntity.getStatusCodeValue()){
                    R body = rResponseEntity.getBody();
                    Map<String, Boolean> data = body.getData(new TypeReference<Map<String, Boolean>>() {});
                    return data.get(item.getKey());
                }
                return false;
            }
        }));
        return R.ok().saveData(collect);
    }

}
