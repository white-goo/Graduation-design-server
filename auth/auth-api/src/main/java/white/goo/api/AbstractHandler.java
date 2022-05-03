package white.goo.api;

import lombok.Getter;
import lombok.Setter;
import white.goo.constant.ValidateContext;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shiyk
 * @date 2022/3/21
 */
@Getter
@Setter
public abstract class AbstractHandler<T> implements IHandler {

    private final List<T> chain;

    private final ValidateContext context;

    public AbstractHandler(ValidateContext context) {
        this.context = context;
        chain = new LinkedList<>();
    }

}
