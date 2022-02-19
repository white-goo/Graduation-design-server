package white.goo.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import white.goo.entity.User;

public class UserUtil {

    public static User getUserInfo(String userId){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        RBucket<Object> bucket = redissonClient.getBucket("user:" + userId);
        return (User) bucket.get();
    }

    public static User getCurrentUser(){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        String token = ThreadLocalUtil.get();
        String userId = JwtUtil.getUserId(token);
        return (User) redissonClient.getBucket("user:" + userId).get();
    }

}
