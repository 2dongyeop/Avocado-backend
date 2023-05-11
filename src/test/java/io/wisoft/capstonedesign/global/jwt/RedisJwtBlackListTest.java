package io.wisoft.capstonedesign.global.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RedisJwtBlackListTest {

    @Autowired private RedisJwtBlackList redisJwtBlackList;
    @Autowired private StringRedisTemplate stringRedisTemplate;

    @Test
    public void addToBlackList_success() throws Exception {
        //given -- 조건
        final String jwt = "example-jwt";

        //when -- 동작
        redisJwtBlackList.addToBlackList(jwt);

        //then -- 검증
        final String selectToken = stringRedisTemplate.opsForValue().get(jwt);
        Assertions.assertThat(selectToken).isNotNull();
    }
}