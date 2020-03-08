package com.feathermind.matrix.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
public class JwtUtil {


    /**
     * 生成签名,默认10min后过期
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String generate(String username, String secret) {
        return generate(username, secret, 10)
    }

    public static String generate(String username, String secret, long expireMinutes) {
        Date expireDate = new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 过期也会抛出异常
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("token校验异常", e)
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            log.error("token校验异常", e)
            return null;
        }
    }
}
