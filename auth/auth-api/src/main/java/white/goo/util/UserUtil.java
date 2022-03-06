package white.goo.util;

import white.goo.constant.RedisKey;
import white.goo.vo.UserVO;

public class UserUtil {

    public static UserVO getUserInfo(String userId){
        CacheUtil cacheUtil = SpringUtil.getBean(CacheUtil.class);
        return (UserVO) cacheUtil.get(RedisKey.User.getValue() + userId);
    }

    public static UserVO getCurrentUser(){
        CacheUtil cacheUtil = SpringUtil.getBean(CacheUtil.class);
        String token = ThreadLocalUtil.get();
        String userId = JwtUtil.getUserId(token);
        return (UserVO) cacheUtil.get(RedisKey.User.getValue() + userId);
    }

}
