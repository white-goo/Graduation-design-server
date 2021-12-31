package white.goo.util;

import com.baomidou.mybatisplus.extension.service.IService;

public class DBUtil {


    public static IService<?> getService(String name){
        return (IService<?>) SpringUtil.getBean(name);
    }

    public static<T extends IService<?>> IService<?> getService(Class<T> type){
        return (IService<?>) SpringUtil.getBean(type);
    }
}
