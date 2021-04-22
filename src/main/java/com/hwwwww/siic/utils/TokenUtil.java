package com.hwwwww.siic.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwwwww.siic.vo.User;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Hwwwww
 */
@Component
public class TokenUtil {
    /**
     * Token时间,单位为毫秒
     */
    private static final long EXPIRE_TIME = 10 * 60 * 1000;

    /**
     * 密钥盐
     */
    private static final String TOKEN_SECRET = "Hwwwww";

    /**
     * 签名生成
     *
     * @param user token
     * @return token
     */
    public static String sign(User user) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名生成
     *
     * @param claim token
     * @return token
     */
    public static String sign(String claim) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", claim)
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static String getInfo(String token) {
        if (token != null && token.length() > 0) {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            try {
                DecodedJWT jwt = verifier.verify(token);
                return jwt.getClaim("username").asString();
            } catch (Exception e) {
                return null;
            }

        } else {
            return null;
        }
    }

    public static int isRefreshToken(String token) {
        Date tokenExpiration;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
        try {
            tokenExpiration = verifier.verify(token).getExpiresAt();
            MyLogger.info("Token到期时间:" + tokenExpiration);
            long diff = tokenExpiration.getTime() - System.currentTimeMillis();
            System.out.println(tokenExpiration.getTime() + " " + System.currentTimeMillis() + " " + diff);
            return diff < 100000 ? 1 : 2;
        } catch (TokenExpiredException e) {
            return 0;
        }
    }


    /**
     * 签名验证
     *
     * @param token token
     * @return boolean
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);

            MyLogger.info("username: " + jwt.getClaim("username").asString() + "  过期时间：" + jwt.getExpiresAt());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}