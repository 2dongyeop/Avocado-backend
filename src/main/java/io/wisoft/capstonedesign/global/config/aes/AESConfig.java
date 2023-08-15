package io.wisoft.capstonedesign.global.config.aes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AESConfig {

    @Value("${aes.key-size}")
    private int aesKeySize;

    @Bean
    Crypto cryptoUtil() throws Exception {
        return new AES(aesKeySize);
    }
}
