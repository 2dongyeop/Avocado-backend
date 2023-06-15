package io.wisoft.capstonedesign.global.jwt;

import io.wisoft.capstonedesign.global.exception.token.InvalidTokenException;
import io.wisoft.capstonedesign.global.exception.token.NotExistTokenException;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JwtTokenProviderTest {

    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired RedisJwtBlackList redisJwtBlackList;
    @Autowired RedisAdapter redisAdapter;

    @Test
    public void createAccessToken_success() throws Exception {
        //given -- 조건
        final String subject = "test-subject";

        //when -- 동작
        final String token = jwtTokenProvider.createAccessToken(subject);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }


    @Test
    public void createRefreshToken_success() throws Exception {
        //given -- 조건
        final String subject = "test";

        //when -- 동작
        final String refreshToken = jwtTokenProvider.createRefreshToken(subject);

        //then -- 검증
        Assertions.assertThat(refreshToken).isNotNull();
    }


    @Test
    public void getSubject_success() throws Exception {
        //given -- 조건
        final String subject = "test-token-subject";
        final String token = jwtTokenProvider.createAccessToken(subject);

        //when -- 동작
        final String getSubject = jwtTokenProvider.getSubject(token);

        //then -- 검증
        Assertions.assertThat(subject).isEqualTo(getSubject);
    }

    @Test
    public void validateToken_success() throws Exception {
        //given -- 조건
        final String subject = "subject";
        final String token = jwtTokenProvider.createAccessToken(subject);

        redisAdapter.setValue(subject, token, 1200000, TimeUnit.SECONDS);

        //when -- 동작
        boolean result = jwtTokenProvider.validateToken(token);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이 요청 헤더에 들어온 경우")
    public void validateToken_fail() throws Exception {
        //given -- 조건
        final String subject = "email@email.com";
        final String refreshToken = jwtTokenProvider.createRefreshToken(subject);

        redisAdapter.setValue(subject, refreshToken, 1200000, TimeUnit.SECONDS);

        //when -- 동작
        //then -- 검증
        assertThrows(InvalidTokenException.class, () -> {
            jwtTokenProvider.validateToken("Invalid-token");
        });
    }
}