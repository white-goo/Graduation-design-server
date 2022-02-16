package white.goo.util;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.dto.Query;

public class DBUtil {


    public static IService<?> getService(String name){
        return (IService<?>) SpringUtil.getBean(name);
    }

    public static<T extends IService<?>> IService<?> getService(Class<T> type){
        return (IService<?>) SpringUtil.getBean(type);
    }

    public static<E> QueryWrapper<E> buildCondition(Query<E> query){
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        if(CollectionUtil.isEmpty(query.getConditions())){
            return queryWrapper;
        }
        query.getConditions().forEach(queryWrapper::like);
        return queryWrapper;
    }
}
