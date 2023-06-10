package io.wisoft.capstonedesign.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisJwtBlackList {

    private final int TIME_OUT = 1 * 60 * 60; //1 hour
    private final RedisTemplate<String, String> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public void addToBlackList(final String jwt) {

        if (isContained(jwt)) {
            stringRedisTemplate.delete(jwt);
        }

        redisTemplate.opsForValue().set(jwt, "blacklisted", TIME_OUT, TimeUnit.SECONDS);
        log.info("redis : 토큰 {}을 로그아웃 처리합니다.", jwt);
    }

    private boolean isContained(final String jwt) {
        return redisTemplate.hasKey(jwt);
    }
}
