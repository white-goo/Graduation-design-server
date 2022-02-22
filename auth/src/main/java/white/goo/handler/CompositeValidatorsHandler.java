package white.goo.handler;

import white.goo.constant.Operator;
import white.goo.serivce.IHandler;

import java.util.LinkedList;
import java.util.Map;

public class CompositeValidatorsHandler implements IHandler<Map<String,Object>> {
    private final Operator opt;

    private final LinkedList<IHandler> chain;

    private CompositeValidatorsHandler(Operator opt) {
        this.opt = opt;
        this.chain = new LinkedList<>();
    }

    public static CompositeValidatorsHandler build(Operator opt) {
        return new CompositeValidatorsHandler(opt);
    }

    @Override
    public boolean doValidate(Map<String, Object> requestParameter) {
        for (IHandler handler : chain) {
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
        chain.add(handler);
        return this;
    }
}
