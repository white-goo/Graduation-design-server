package white.goo.handler;

import white.goo.api.AbstractHandler;
import white.goo.api.IValidator;
import white.goo.constant.Operator;
import white.goo.constant.ValidateContext;

public class ValidatorsHandler extends AbstractHandler<IValidator<?>> {

    private ValidatorsHandler(ValidateContext context) {
        super(context);
    }

    public static ValidatorsHandler build(ValidateContext validateContext) {
        return new ValidatorsHandler(validateContext);
    }

    @Override
    public boolean doValidate(String requestParameter) {
        ValidateContext context = getContext();
        Operator opt = context.getOpt();
        for (int i = 0; i < getChain().size(); i++) {
            boolean b = getChain().get(i).chain(context, requestParameter, context.getParam().get(i));
            if (Operator.AND == opt && !b) {
                return false;
            } else if (Operator.OR == opt && b) {
                return true;
            }
        }
        return Operator.AND == opt;
    }

    public ValidatorsHandler addValidator(IValidator<?> iValidator) {
        getChain().add(iValidator);
        return this;
    }

}
