package white.goo.handler;

import com.alibaba.fastjson.JSONObject;
import white.goo.constant.Operator;
import white.goo.constant.ValidateContext;
import white.goo.serivce.IHandler;
import white.goo.serivce.IValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ValidateHandler implements IHandler {

    private final ValidateContext context;

    public ValidateContext getContext() {
        return context;
    }

    private final LinkedList<IValidator> chain;

    private ValidateHandler(ValidateContext context) {
        this.context = context;
        this.chain = new LinkedList<>();
    }

    public static ValidateHandler build(Operator opt) {
        ValidateContext validateContext = new ValidateContext(new ArrayList<>(), opt);
        return new ValidateHandler(validateContext);
    }

    @Override
    public boolean doValidate(JSONObject requestParameter) {

        Operator opt = context.getOpt();
        for (int i = 0; i < chain.size(); i++) {
            boolean b = chain.get(i).doValidate(context, requestParameter,context.getParam().get(i));
            if(Operator.AND == opt && !b){
                return false;
            }else if(Operator.OR == opt && b){
                return true;
            }
        }
        return Operator.AND == opt;
    }

    public ValidateHandler add(IValidator iValidator) {
        chain.add(iValidator);
        return this;
    }

}
