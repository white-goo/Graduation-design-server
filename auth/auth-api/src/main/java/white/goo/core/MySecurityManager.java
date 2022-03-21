package white.goo.core;

import com.alibaba.fastjson.JSON;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import white.goo.annonation.*;
import white.goo.api.IHandler;
import white.goo.api.IValidator;
import white.goo.config.RepeatedlyReadRequestWrapper;
import white.goo.constant.Operator;
import white.goo.constant.ValidateContext;
import white.goo.filter.BackGroundJwtValidator;
import white.goo.filter.JwtValidator;
import white.goo.handler.CompositeValidatorsHandler;
import white.goo.handler.ValidatorsHandler;
import white.goo.util.SpringUtil;
import white.goo.util.UserUtil;
import white.goo.vo.AuthCheckVO;
import white.goo.vo.UserVO;

import javax.annotation.PostConstruct;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class MySecurityManager {

    private final Map<String, IHandler> handlerMap;

    private final List<String> nonValidateURI;

    public MySecurityManager() {
        this.handlerMap = new HashMap<>(16);
        this.nonValidateURI = new ArrayList<>(10);
    }

    public boolean doValidate(RepeatedlyReadRequestWrapper requestWrapper, ServletResponse response) {

        HttpServletRequest request = (HttpServletRequest) requestWrapper.getRequest();

        Map<String, Object> map = new HashMap<>();
        map.put("request", requestWrapper);
        map.put("response", response);

        IValidator<Map<String, Object>> backGroundJwtValidator = (BackGroundJwtValidator) SpringUtil.getBean("backGroundJwtValidator");
        if(backGroundJwtValidator.doValidate(null, map, null)){
            return true;
        }

        String requestURI = request.getRequestURI();
        String collect = null;
        String method = request.getMethod();
        String contentType = request.getContentType();
        if ("POST".equals(method) && "application/json;charset=UTF-8".equals(contentType)) {
            BufferedReader reader = requestWrapper.getReader();
            collect = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }

        boolean contains = nonValidateURI.contains(requestURI);
        if (!contains) {
            IValidator<Map<String, Object>> jwt = (JwtValidator) SpringUtil.getBean("jwt");
            if (!jwt.doValidate(null, map, null)) {
                return false;
            }
            //判断是系统内置用户超管用户,直接放行,判断机制待完善
            UserVO currentUser = UserUtil.getCurrentUser();
            if ("admin".equals(currentUser.getUsername())) {
                return true;
            }
            IHandler iHandler = handlerMap.get(requestURI);
            if (Objects.nonNull(iHandler)) {
                return iHandler.doValidate(collect);
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
                if (Objects.nonNull(compositeValidators)) {
                    ValidateContext validateContext = new ValidateContext(compositeValidators.opt());
                    validateContext.setSecurityManager(this);
                    CompositeValidatorsHandler compositeValidatorsHandler = CompositeValidatorsHandler.build(validateContext);
                    for (AuthValidators authValidators : compositeValidators.value()) {
                        ValidatorsHandler handler = parseAuthValidators(authValidators);
                        compositeValidatorsHandler.addHandler(handler);
                    }
                    handlerMap.put(uri, compositeValidatorsHandler);
                } else {
                    AuthValidators authValidators = v.getMethod().getAnnotation(AuthValidators.class);
                    if (Objects.nonNull(authValidators)) {
                        ValidatorsHandler handler = parseAuthValidators(authValidators);
                        handlerMap.put(uri, handler);
                    } else {
                        AuthValidator authValidator = v.getMethod().getAnnotation(AuthValidator.class);
                        if (Objects.nonNull(authValidator)) {
                            ValidateContext validateContext = new ValidateContext(Operator.AND);
                            validateContext.setSecurityManager(this);
                            ValidatorsHandler handler = ValidatorsHandler.build(validateContext);
                            parseAuthValidator(handler, authValidator);
                            handlerMap.put(uri, handler);
                        }
                    }
                }
            }
        });

    }

    private ValidatorsHandler parseAuthValidators(AuthValidators annotation) {
        ValidateContext validateContext = new ValidateContext(annotation.opt());
        validateContext.setSecurityManager(this);
        ValidatorsHandler handler = ValidatorsHandler.build(validateContext);
        AuthValidator[] value = annotation.value();
        for (AuthValidator authValidator : value) {
            parseAuthValidator(handler, authValidator);
        }
        return handler;
    }

    private void parseAuthValidator(ValidatorsHandler handler, AuthValidator authValidator) {
        String id = authValidator.value();
        IValidator<?> iValidator = SpringUtil.getApplicationContext().getBeansOfType(IValidator.class).get(id);
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

    public boolean authCheck(AuthCheckVO authCheckVO) {
        String path = authCheckVO.getPath();
        UserVO currentUser = UserUtil.getCurrentUser();
        boolean contains = nonValidateURI.contains(path);
        if (!contains) {
            if ("admin".equals(currentUser.getUsername())) {
                return true;
            }
            IHandler iHandler = handlerMap.get(path);
            if (Objects.nonNull(iHandler)) {
                return iHandler.doValidate(JSON.toJSONString(authCheckVO.getParams()));
            }
            return false;
        }
        return true;
    }
}
