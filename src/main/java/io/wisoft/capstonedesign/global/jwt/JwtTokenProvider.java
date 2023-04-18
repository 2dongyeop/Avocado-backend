package io.wisoft.capstonedesign.global.jwt;

import io.jsonwebtoken.*;

import io.wisoft.capstonedesign.global.exception.token.NotExistTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private String secretKey;
    private long validityInMilliseconds;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private Set<String> blackList = new HashSet<>();

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final String subject) {
        Claims claims = Jwts.claims().setSubject(subject);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        logger.info("now: {}", now);
        logger.info("validity: {}", validity);


        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("생성된 JWT: {}", token);
        return token;
    }

    /** 토큰에서 값 추출 */
    public String getSubject(final String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** 유효한 토큰인지 확인 */
    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            /** 만료시간이 지났을 경우 */
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            /** 로그아웃 요청한 토큰(= 블랙리스트에 포함)일 경우 */
            if (blackList.contains(token)) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void addBlackList(final String token) throws IllegalAccessException {

        if (blackList.contains(token)) {
            logger.error("이미 로그아웃된 토큰입니다.");
            throw new IllegalAccessException("이미 로그아웃된 토큰입니다.");
        }

        if (!validateToken(token)) {
            logger.error("유효하지 않은 토큰");
            throw new NotExistTokenException("토큰이 존재하지 않음");
        }

        blackList.add(token);
    }
}
