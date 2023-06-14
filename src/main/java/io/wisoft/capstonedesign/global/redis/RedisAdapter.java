package io.wisoft.capstonedesign.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisAdapter {

    private final StringRedisTemplate redisTemplate;

    public void setValue(final String key, final String value, final long timeout, final TimeUnit unit) {
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, timeout, unit);
    }

    public String getValue(final String key) {
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValue(final String key) {
        redisTemplate.delete(key);
    }


    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }
}
