package white.goo.handler;

import white.goo.api.AbstractHandler;
import white.goo.api.IHandler;
import white.goo.constant.Operator;
import white.goo.constant.ValidateContext;

public class CompositeValidatorsHandler extends AbstractHandler<IHandler> {

    private CompositeValidatorsHandler(ValidateContext context) {
        super(context);
    }

    public static CompositeValidatorsHandler build(ValidateContext context) {
        return new CompositeValidatorsHandler(context);
    }

    @Override
    public boolean doValidate(String requestParameter) {
        Operator opt = getContext().getOpt();
        for (IHandler handler : getChain()) {
            boolean b = handler.doValidate(requestParameter);
            if (Operator.AND == opt && !b) {
                return false;
            } else if (Operator.OR == opt && b) {
                return true;
            }
        }
        return Operator.AND == opt;
    }

    public CompositeValidatorsHandler addHandler(IHandler handler){
        getChain().add(handler);
        return this;
    }
}
