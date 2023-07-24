package io.wisoft.capstonedesign.global.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChatGptBucketConfig {

    @Bean
    public Bucket chatGptBucket() {

        //60초에 3개의 토큰씩 충전
        final Refill refill = Refill.intervally(3, Duration.ofSeconds(60));

        //버킷의 크기는 3개
        final Bandwidth limit = Bandwidth.classic(3, refill);

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
