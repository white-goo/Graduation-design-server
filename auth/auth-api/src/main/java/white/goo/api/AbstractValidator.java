package white.goo.api;

import com.alibaba.fastjson.JSON;
import org.springframework.core.ResolvableType;
import white.goo.constant.ValidateContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author shiyk
 * @date 2022/3/21
 */
public abstract class AbstractValidator<T> implements IValidator<T> {

    private final Type genericSuperclass = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public T convertDate(String date) {
        return JSON.parseObject(date, this.genericSuperclass);
    }

    @Override
    public boolean chain(ValidateContext ctx, String requestParam, Map<String, List<String[]>> param) {
        T t = this.convertDate(requestParam);
        return doValidate(ctx, t, param);
    }

}
