package com.merkle.wechat.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author tyao
 *
 */
@Component
public class JwtMiniProgramUtil {
    private static String SECRET;
    private static long EXPIREDTIME;// ms

    private static String ENV;

    // keys
    private static final String CLAIM_KEY_USERID = "openid";
    private static final String CLAIM_KEY_ENV = "env";

    // error msg constant
    private static final String TOKEN_EXPIRED = "token expired!";
    private static final String INVALID_TOKEN = "invalid token!";

    public static String generateToken(String openid) throws AuthenticationException {
        isValidData(openid);

        Date expiredTime = new Date(System.currentTimeMillis() + EXPIREDTIME);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, openid);
        claims.put(CLAIM_KEY_ENV, ENV);

        return Jwts.builder().setClaims(claims).setExpiration(expiredTime).signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static String parseToken(String token) throws AuthenticationException {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String userId = claims.get(CLAIM_KEY_USERID, String.class);
            String env = claims.get(CLAIM_KEY_ENV, String.class);

            isValidData(userId);

            if (!env.equals(ENV)) {
                throw new AuthenticationServiceException(INVALID_TOKEN);
            }

            if (claims.getExpiration().after(new Date())) {
                return userId;
            } else {
                throw new AuthenticationServiceException(TOKEN_EXPIRED);
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    private static void isValidData(String openid) throws AuthenticationException {
        if (StringUtils.isEmpty(openid)) {
            throw new AuthenticationServiceException(INVALID_TOKEN);
        }
    }

    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${spring.profiles.active}")
    private void setEnv(String active) {
        ENV = active;
    }

    @Value("${jwt.expiredtime}")
    private void setExpiredDate(long expiredTime) {
        EXPIREDTIME = expiredTime * 1000;
    }
}
