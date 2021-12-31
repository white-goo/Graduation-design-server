package white.goo.util;

import com.baomidou.mybatisplus.extension.service.IService;

public class DBUtil {


    public static IService<?> getService(String name){
        return (IService<?>) SpringUtil.getBean(name);
    }

    public static<T extends IService<?>> IService<T> getService(Class<T> type){
        return (IService<T>) SpringUtil.getBean(type);
    }
}
