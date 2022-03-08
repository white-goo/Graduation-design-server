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

    public static void update(UserVO userVO){
        CacheUtil cacheUtil = SpringUtil.getBean(CacheUtil.class);
        cacheUtil.update(RedisKey.User.getValue() + userVO.getId(), userVO);
    }

}
