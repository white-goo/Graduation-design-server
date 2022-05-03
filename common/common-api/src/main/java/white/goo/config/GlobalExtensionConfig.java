package white.goo.config;

import com.alibaba.fastjson.JSON;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import white.goo.annotation.GlobalExtensionPoint;
import white.goo.api.impl.GlobalExtension;
import white.goo.dto.PointProxyView;
import white.goo.util.JwtUtil;
import white.goo.util.SpringUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shiyk
 * @date 2022/5/3
 */
@Component
public class GlobalExtensionConfig implements ApplicationRunner,Serializable {

    @Value("${spring.application.name}")
    private String module;

    @Autowired
    private RedissonClient redissonClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> beansWithAnnotation = SpringUtil.getApplicationContext().getBeansWithAnnotation(GlobalExtensionPoint.class);
        Map<String, Object> collect = beansWithAnnotation.entrySet().stream().collect(Collectors.toMap(entity -> ClassUtils.getUserClass(entity.getValue()).getName(), Map.Entry::getValue));
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringUtil.getApplicationContext().getBean(RequestMappingHandlerMapping.class);

        Map<String, Map<String, String>> uriList = new HashMap<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            String name = value.getBeanType().getName();
            Object o = collect.get(name);
            if (o != null) {
                Map<String, String> orDefault = uriList.getOrDefault(name, new HashMap<>());
                orDefault.put(value.getMethod().getName(), key.getPatternsCondition().getPatterns().toArray()[0].toString());
                uriList.put(name, orDefault);
            }
        });

        collect.forEach((k, v) -> {
            Class<?> aClass = ClassUtils.getUserClass(v);
            Annotation[] annotations = aClass.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
                GlobalExtensionPoint globalExtensionPoint = annotationType.getAnnotation(GlobalExtensionPoint.class);
                if (globalExtensionPoint != null) {

                    Class<?> baseOn = globalExtensionPoint.baseOn();
                    String label = globalExtensionPoint.label();
                    Class<?> config = globalExtensionPoint.config();
                    Field[] declaredFields = config.getDeclaredFields();
                    GlobalExtension globalExtension = new GlobalExtension(uriList.get(k), baseOn);

                    Object configBean = null;
                    try {
                        configBean = config.newInstance();
                        for (Field declaredField : declaredFields) {
                            declaredField.setAccessible(true);
                            String name = declaredField.getName();
                            Object o = annotationAttributes.get(name);
                            declaredField.set(configBean, o);
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                    }

                    globalExtension.setLabel(label);
                    globalExtension.setConfig(configBean);
                    globalExtension.setModule(module);
                    String id = (String) annotationAttributes.get("id");
                    globalExtension.setId(id);
                    if (Objects.isNull(globalExtension.getId())) {
                        globalExtension.setId(aClass.getSimpleName());
                    }
                    RMap<Object, Object> map = redissonClient.getMap("point:" + annotationType.getName());
                    map.put(module, globalExtension);
                }
            }
        });
    }
}
