package white.goo.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import white.goo.annonation.*;
import white.goo.config.RepeatedlyReadRequestWrapper;
import white.goo.constant.Operator;
import white.goo.entity.User;
import white.goo.handler.CompositeValidatorsHandler;
import white.goo.handler.ValidatorsHandler;
import white.goo.serivce.IHandler;
import white.goo.serivce.IValidator;
import white.goo.util.SpringUtil;
import white.goo.util.UserUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class SecurityManager {

    private final Map<String, IHandler> handlerMap;

    private final List<String> nonValidateURI;

    public SecurityManager() {
        this.handlerMap = new HashMap<>(16);
        this.nonValidateURI = new ArrayList<>(10);
    }

    public boolean doValidate(RepeatedlyReadRequestWrapper requestWrapper) {

        HttpServletRequest request = (HttpServletRequest) requestWrapper.getRequest();
        BufferedReader reader = requestWrapper.getReader();
        String requestURI = request.getRequestURI();
        String collect = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject requestParam = JSON.parseObject(collect);
        boolean contains = nonValidateURI.contains(requestURI);
        if (!contains) {
            IValidator jwt = (IValidator) SpringUtil.getBean("jwt");
            boolean b = jwt.doValidate(null, null, null);
            if(b){
                //判断是系统内置用户超管用户,直接放行,判断机制待完善
                User currentUser = UserUtil.getCurrentUser();
                if ("1".equals(currentUser.getId())) {
                    return true;
                }
                IHandler iHandler = handlerMap.get(requestURI);
                if (Objects.nonNull(iHandler)) {
                    return iHandler.doValidate(requestParam);
                }
            }
        }
        return true;
    }

    @PostConstruct
    public void init() {

        RequestMappingHandlerMapping bean = SpringUtil.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            String uri = (String) k.getPatternsCondition().getPatterns().toArray()[0];
            AnonValidate anonValidate = v.getMethod().getAnnotation(AnonValidate.class);
            if (Objects.nonNull(anonValidate)) {
                nonValidateURI.add(uri);
            } else {
                CompositeValidators compositeValidators = v.getMethod().getAnnotation(CompositeValidators.class);
                if(Objects.nonNull(compositeValidators)){
                    CompositeValidatorsHandler compositeValidatorsHandler = CompositeValidatorsHandler.build(compositeValidators.opt());
                    for (AuthValidators authValidators : compositeValidators.value()) {
                        ValidatorsHandler handler = parseAuthValidators(authValidators);
                        compositeValidatorsHandler.addHandler(handler);
                    }
                    handlerMap.put(uri, compositeValidatorsHandler);
                }else {
                    AuthValidators authValidators = v.getMethod().getAnnotation(AuthValidators.class);
                    if (Objects.nonNull(authValidators)) {
                        ValidatorsHandler handler = parseAuthValidators(authValidators);
                        handlerMap.put(uri, handler);
                    } else {
                        AuthValidator authValidator = v.getMethod().getAnnotation(AuthValidator.class);
                        if (Objects.nonNull(authValidator)) {
                            ValidatorsHandler handler = ValidatorsHandler.build(Operator.AND);
                            parseAuthValidator(handler, authValidator);
                            handlerMap.put(uri, handler);
                        }
                    }
                }
            }
        });

    }

    private ValidatorsHandler parseAuthValidators(AuthValidators annotation) {
        ValidatorsHandler handler = ValidatorsHandler.build(annotation.opt());
        AuthValidator[] value = annotation.value();
        for (AuthValidator authValidator : value) {
            parseAuthValidator(handler, authValidator);
        }
        return handler;
    }

    private void parseAuthValidator(ValidatorsHandler handler, AuthValidator authValidator) {
        String id = authValidator.value();
        IValidator iValidator = (IValidator) SpringUtil.getBean(id);
        handler.addValidator(iValidator);
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
