package white.goo.dto;

import com.alibaba.fastjson.JSON;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import white.goo.util.JwtUtil;
import white.goo.util.SpringUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

/**
 * @author shiyk
 * @date 2022/5/3
 */
public class PointProxyView implements InvocationHandler, Serializable {

    private final Map<String, String> uriList;
    private final String module;

    public static Object newInstance(Class<?> cl, Map<String, String> uriList, String module) {
        return Proxy.newProxyInstance(PointProxyView.class.getClassLoader(),new Class[]{cl},new PointProxyView(uriList, module));
    }

    public PointProxyView(Map<String, String> uriList, String module) {
        this.uriList = uriList;
        this.module = module;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        DiscoveryClient discoveryClient = SpringUtil.getApplicationContext().getBean(DiscoveryClient.class);
        RestTemplate restTemplate = SpringUtil.getApplicationContext().getBean(RestTemplate.class);

        ServiceInstance serviceInstance = discoveryClient.getInstances(module).get(discoveryClient.getOrder());
        URI uri = serviceInstance.getUri();
        String uri2 = uriList.get(method.getName());
        if (uri2 == null) {
            return method.invoke(objects);
        }
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> multiValueMapHttpEntity = null;
        headers.set("backGroundToken", JwtUtil.signBackGroundToken());
        if(method.getParameterCount() == 0){
            multiValueMapHttpEntity = new HttpEntity<>(null, headers);
        }else {
            multiValueMapHttpEntity = new HttpEntity<>(objects[0], headers);
        }
        Class<?> returnType = method.getReturnType();
        ResponseEntity<?> exchange = restTemplate.exchange(uri + uri2,
                HttpMethod.POST,
                multiValueMapHttpEntity,
                returnType);
        if (200 != exchange.getStatusCodeValue()) {
            throw new Exception(o.toString() + " 调用方法: " + method.getName() + " 出现错误: " + JSON.toJSON(exchange.getBody()));
        }
        return exchange.getBody();
    }
}
