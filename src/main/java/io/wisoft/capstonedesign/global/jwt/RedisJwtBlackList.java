package io.wisoft.capstonedesign.global.jwt;

import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisJwtBlackList {

    private final int TIME_OUT = 1 * 60 * 60; //1 hour
    private final RedisAdapter redisAdapter;
    private final String LOGOUT_STATUS = "LOGOUT_STATUS";

    public void addToBlackList(final String jwt) {

        if (isContained(jwt)) {
            redisAdapter.deleteValue(jwt);
        }

        redisAdapter.setValue(jwt, LOGOUT_STATUS, TIME_OUT, TimeUnit.SECONDS);
        log.info("redis : 토큰 {}을 로그아웃 처리합니다.", jwt);
    }

    private boolean isContained(final String jwt) {
        return redisAdapter.hasKey(jwt);
    }
}
