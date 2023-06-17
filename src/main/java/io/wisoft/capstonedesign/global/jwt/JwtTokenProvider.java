package io.wisoft.capstonedesign.global.jwt;

import io.jsonwebtoken.*;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.token.AlreadyLogoutException;
import io.wisoft.capstonedesign.global.exception.token.InvalidTokenException;
import io.wisoft.capstonedesign.global.exception.token.ExpiredTokenException;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Log4j2
@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long ACCESS_TOKEN_EXPIRE_SECOND;
    private final long REFRESH_TOKEN_EXPIRE_SECOND;
    private final RedisAdapter redisAdapter;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.access-expire-length}") final long ACCESS_TOKEN_EXPIRE_SECOND,
            @Value("${security.jwt.token.refresh-expire-length}") final long REFRESH_TOKEN_EXPIRE_SECOND,
            final RedisAdapter redisAdapter) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.ACCESS_TOKEN_EXPIRE_SECOND = ACCESS_TOKEN_EXPIRE_SECOND;
        this.REFRESH_TOKEN_EXPIRE_SECOND = REFRESH_TOKEN_EXPIRE_SECOND;
        this.redisAdapter = redisAdapter;
    }

    public String createAccessToken(final String subject) {
        final String token = getToken(subject, ACCESS_TOKEN_EXPIRE_SECOND);

        log.info("생성된 accessToken: {}", token);
        return token;
    }


    public String createRefreshToken(final String subject) {
        final String token = getToken(subject, REFRESH_TOKEN_EXPIRE_SECOND);

        log.info("생성된 refreshToken: {}", token);
        return token;
    }

    private String getToken(final String subject, final long expiredSecond) {
        final Claims claims = Jwts.claims().setSubject(subject);

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + expiredSecond);

        final String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }


    /**
     * 토큰에서 값 추출
     */
    public String getSubject(final String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(final String email) {

        /** 유효하지 않은 토큰일 경우 */
        isValidToken(email);

        /** 로그아웃 처리된 토큰으로 요청할 경우 */
        validIsAlreadyLogout(email);

        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(email);

            /** 만료시간이 지났을 경우 */
            isExpiredToken(claims);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            /* do nothing! */
            return false;
        }
    }

    private void isValidToken(final String email) {

        if (!redisAdapter.hasKey(email)) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.", ErrorCode.INVALID_TOKEN);
        }
    }


    private void isExpiredToken(final Jws<Claims> claims) {

        if (claims.getBody().getExpiration().before(new Date())) {
            throw new ExpiredTokenException("만료시간이 지난 토큰입니다.", ErrorCode.EXPIRED_TOKEN);
        }
    }


    private void validIsAlreadyLogout(final String email) {

        final String value = redisAdapter.getValue(email);
        System.out.println("value = " + value);

        if (value.equals("LOGOUT_STATUS")) {
            throw new AlreadyLogoutException("로그아웃 처리된 토큰입니다.", ErrorCode.ALREADY_LOGOUT_TOKEN);
        }
    }
}
