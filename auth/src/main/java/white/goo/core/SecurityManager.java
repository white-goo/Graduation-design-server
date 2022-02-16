package white.goo.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import white.goo.annonation.*;
import white.goo.constant.Operator;
import white.goo.handler.ValidateHandler;
import white.goo.serivce.IHandler;
import white.goo.serivce.IValidator;
import white.goo.util.SpringUtil;
import white.goo.util.UserUtil;
import white.goo.vo.UserVO;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SecurityManager {

    private Map<String, IHandler> handlerMap;

    private List<String> nonValidateURI;

    public SecurityManager() {
        this.handlerMap = new HashMap<>(16);
        this.nonValidateURI = new ArrayList<>(10);
    }

    public boolean doValidate() throws IOException {

        //判断时系统内置用户超管用户,直接放行,判断机制待完善
        UserVO currentUser = UserUtil.getCurrentUser();
        if("1".equals(currentUser.getId())){
            return true;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestURI = request.getRequestURI();
        String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject requestParam = JSON.parseObject(collect);
        boolean contains = nonValidateURI.contains(requestURI);
        if(!contains){
            IHandler iHandler = handlerMap.get(requestURI);
            if(Objects.nonNull(iHandler)){
                return iHandler.doValidate(requestParam);
            }
        }
        return true;
    }

    @PostConstruct
    public void init(){

        RequestMappingHandlerMapping bean = SpringUtil.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        handlerMethods.forEach((k,v)->{
            String uri = (String) k.getPatternsCondition().getPatterns().toArray()[0];
            Login login = v.getMethod().getAnnotation(Login.class);
            if(Objects.nonNull(login)){
                nonValidateURI.add(uri);
            }else {
                AuthValidators authValidators = v.getMethod().getAnnotation(AuthValidators.class);
                if(Objects.nonNull(authValidators)){
                    ValidateHandler handler = parseAuthValidators(authValidators);
                    handlerMap.put(uri, handler);
                }else {
                    AuthValidator authValidator = v.getMethod().getAnnotation(AuthValidator.class);
                    if(Objects.nonNull(authValidator)){
                        ValidateHandler handler = ValidateHandler.build(Operator.AND);
                        parseAuthValidator(handler,authValidator);
                        handlerMap.put(uri, handler);
                    }
                }
            }
        });

    }

    private ValidateHandler parseAuthValidators(AuthValidators annotation) {
        ValidateHandler handler = ValidateHandler.build(annotation.opt());
        AuthValidator[] value = annotation.value();
        for (AuthValidator authValidator : value) {
            parseAuthValidator(handler, authValidator);
        }
        return handler;
    }

    private void parseAuthValidator(ValidateHandler handler, AuthValidator authValidator){
        String id = authValidator.value();
        IValidator iValidator = (IValidator) SpringUtil.getBean(id);
        handler.add(iValidator);
        HashMap<String, List<String[]>> paramMap = new HashMap<>();
        ValidateParam[] param = authValidator.param();
        for (ValidateParam validateParam : param) {
            List<String[]> params = paramMap.getOrDefault(validateParam.name(), new ArrayList<>(3));
            params.add(validateParam.value());
            paramMap.putIfAbsent(validateParam.name(), params);
        }
        handler.getContext().getParam().add(paramMap);
    }
}
