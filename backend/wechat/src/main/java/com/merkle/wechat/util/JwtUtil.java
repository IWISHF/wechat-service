package com.merkle.wechat.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.merkle.wechat.security.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author tyao
 *
 */
@Component
public class JwtUtil {
    private static String SECRET;
    private static long EXPIREDTIME;// ms

    private static String ENV;

    // keys
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_USERID = "userId";
    private static final String CLAIM_KEY_PBNOIDS = "pbNoIds";
    private static final String CLAIM_KEY_ENV = "env";

    // error msg constant
    private static final String TOKEN_EXPIRED = "token expired!";
    private static final String INVALID_TOKEN = "invalid token!";

    public static String generateToken(UserInfo userInfo) throws AuthenticationException {
        isValidData(userInfo);

        Date expiredTime = new Date(System.currentTimeMillis() + EXPIREDTIME);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, userInfo.getUserId());
        claims.put(CLAIM_KEY_PBNOIDS, userInfo.getPbNoIds());
        claims.put(CLAIM_KEY_ROLE, userInfo.getRole());
        claims.put(CLAIM_KEY_ENV, ENV);

        return Jwts.builder().setClaims(claims).setExpiration(expiredTime).signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static UserInfo parseToken(String token) throws AuthenticationException {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String role = claims.get(CLAIM_KEY_ROLE, String.class);
            String userId = claims.get(CLAIM_KEY_USERID, String.class);
            String pbNoIds = claims.get(CLAIM_KEY_PBNOIDS, String.class);
            String env = claims.get(CLAIM_KEY_ENV, String.class);
            UserInfo userInfo = new UserInfo(userId, pbNoIds, role);

            isValidData(userInfo);

            if (!env.equals(ENV)) {
                throw new AuthenticationServiceException(INVALID_TOKEN);
            }

            if (claims.getExpiration().after(new Date())) {
                return userInfo;
            } else {
                throw new AuthenticationServiceException(TOKEN_EXPIRED);
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    private static void isValidData(UserInfo userInfo) throws AuthenticationException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getRole())) {
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
