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

    public void addToBlackList(final String email) {

        if (isContained(email)) {
            redisAdapter.deleteValue(email);
        }

        redisAdapter.setValue(email, LOGOUT_STATUS, TIME_OUT, TimeUnit.SECONDS);
        log.info("redis : {}을 로그아웃 처리합니다.", email);
    }

    public boolean isContained(final String email) {
        return redisAdapter.hasKey(email);
    }
}
