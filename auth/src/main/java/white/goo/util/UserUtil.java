package white.goo.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import white.goo.constant.RedisKey;
import white.goo.vo.UserVO;

public class UserUtil {

    public static UserVO getUserInfo(String userId){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        RBucket<Object> bucket = redissonClient.getBucket(RedisKey.User.getValue() + userId);
        return (UserVO) bucket.get();
    }

    public static UserVO getCurrentUser(){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        String token = ThreadLocalUtil.get();
        String userId = JwtUtil.getUserId(token);
        return (UserVO) redissonClient.getBucket(RedisKey.User.getValue() + userId).get();
    }

}
