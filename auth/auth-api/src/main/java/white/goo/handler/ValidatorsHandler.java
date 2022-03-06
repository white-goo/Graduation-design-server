package white.goo.handler;

import white.goo.api.IHandler;
import white.goo.api.IValidator;
import white.goo.constant.Operator;
import white.goo.constant.ValidateContext;

import java.util.LinkedList;
import java.util.Map;

public class ValidatorsHandler implements IHandler<Map<String,Object>> {

    private final ValidateContext context;

    private final LinkedList<IValidator> chain;

    public ValidateContext getContext() {
        return context;
    }


    private ValidatorsHandler(ValidateContext context) {
        this.context = context;
        this.chain = new LinkedList<>();
    }

    public static ValidatorsHandler build(ValidateContext validateContext) {
        return new ValidatorsHandler(validateContext);
    }

    @Override
    public boolean doValidate(Map<String,Object> requestParameter) {

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

    public ValidatorsHandler addValidator(IValidator iValidator) {
        chain.add(iValidator);
        return this;
    }

}
