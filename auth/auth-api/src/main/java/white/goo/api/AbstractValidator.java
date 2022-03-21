package white.goo.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import white.goo.constant.ValidateContext;

import java.util.List;
import java.util.Map;

/**
 * @author shiyk
 * @date 2022/3/21
 */
public abstract class AbstractValidator<T> implements IValidator<T> {

    @Override
    public T convertDate(String date) {
        return JSON.parseObject(date,new TypeReference<T>(){});
    }

    @Override
    public boolean chain(ValidateContext ctx, String requestParam, Map<String, List<String[]>> param) {
        T t = convertDate(requestParam);
        return doValidate(ctx, t, param);
    }

}
