package white.goo.util;

import org.apache.shiro.SecurityUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import white.goo.vo.UserVo;

public class UserUtil {

    public static UserVo getUserInfo(Long userId){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        RBucket<Object> bucket = redissonClient.getBucket("user:" + userId);
        return (UserVo) bucket.get();
    }

    public static UserVo getCurrentUser(){
        RedissonClient redissonClient = (RedissonClient) SpringUtil.getBean(RedissonClient.class);
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        Long userId = JwtUtil.getUserId(token);
        return (UserVo) redissonClient.getBucket("user:" + userId).get();
    }

}
