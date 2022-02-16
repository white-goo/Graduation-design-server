package white.goo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    private static final String SING = "White-goo";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static Boolean verify(String token) {
        //根据密码生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(SING);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("userId", JwtUtil.getUserId(token))
                .withClaim("permission", JwtUtil.getPermission(token))
                .build();
        //效验TOKEN
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getPermission(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("permission").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得tokenId
     *
     * @return uuid
     */
    public static String getTokenId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getId();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token过期时间
     *
     * @return 过期时间
     */
    public static Date getExpiresAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token签发时间
     *
     * @return 签发时间
     */
    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名
     *
     *
     * @param userId 用户id
     * @Param permission 权限
     * @return 加密的token
     */
    public static String sign(String userId, String permission) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SING);
        String jwtId = UUID.randomUUID().toString();
        // 附带username信息
        return JWT.create()
                .withJWTId(jwtId)
                .withClaim("userId", userId)
                .withClaim("permission", permission)
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }
}