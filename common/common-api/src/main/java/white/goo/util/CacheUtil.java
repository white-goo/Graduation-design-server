package white.goo.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CacheUtil {

    private final Cache<String, Object> cache;

    @Autowired
    private RedissonClient redissonClient;

    public CacheUtil() {
        cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    public Object get(String key){
        Object result = cache.getIfPresent(key);
        if(Objects.isNull(result)){
            RBucket<Object> bucket = redissonClient.getBucket(key);
            Object o = bucket.get();
            cache.put(key, o);
            return o;
        }
        return result;
    }

    public void set(String key, Object value){
        cache.put(key, value);
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    public void remove(String key){
        cache.invalidate(key);
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    public void update(String key, Object value) {
        cache.put(key, value);
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

}
