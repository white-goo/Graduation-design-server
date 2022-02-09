package white.goo.annonation.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import white.goo.annonation.ValidatePermission;
import white.goo.constant.Operator;
import white.goo.dto.R;
import white.goo.util.JwtUtil;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class ValidatePermissionAspect {

    @Pointcut("@annotation(white.goo.annonation.ValidatePermission)")
    public void point(){}

    @Around(value = "point()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> aClass = joinPoint.getTarget().getClass();
        String name = joinPoint.getSignature().getName();
        Method[] methods = aClass.getMethods();
        Method method = null;
        for (Method method1 : methods) {
            if(name.equals(method1.getName())){
                method = method1;
            }
        }
        if(Objects.nonNull(method)){
            ValidatePermission annotation = method.getAnnotation(ValidatePermission.class);
            String[] value = annotation.value();
            Operator operator = annotation.operator();
            boolean b = hasPermission(value, operator);
            if(b){
                return joinPoint.proceed();
            }
        }
        return R.error("无权限访问当前页面");
    }

    private boolean hasPermission(String[] permissions,Operator operator){

        String token = (String) SecurityUtils.getSubject().getPrincipal();
        JSONArray objects = JSON.parseArray(JwtUtil.getPermission(token));
        for (String permission : permissions) {
            if(!objects.contains(permission)){
                if(Operator.AND == operator){
                    return false;
                }
            }else {
                if(Operator.OR == operator){
                    return true;
                }
            }
        }
        return Operator.AND == operator;
    }

}
