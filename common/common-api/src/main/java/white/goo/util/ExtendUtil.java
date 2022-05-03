package white.goo.util;

import cn.hutool.core.collection.CollectionUtil;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import white.goo.api.Extension;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author shiyk
 * @date 2022/5/3
 */
public class ExtendUtil {

    public static List<Extension> getPoint(Class<? extends Annotation> pointClass){
        RedissonClient redissonClient = SpringUtil.getApplicationContext().getBean(RedissonClient.class);
        RMap<String, Extension> map = redissonClient.getMap("point:" + pointClass.getName());
        Collection<Extension> values = null;
        if(map.isExists() && CollectionUtil.isNotEmpty((values = map.values()))){
            return new ArrayList<>(values);
        }
        return new ArrayList<>(0);
    }

}
