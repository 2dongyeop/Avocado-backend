package io.wisoft.capstonedesign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BcryptConfig {
    @Bean
    EncryptHelper encryptHelper() {
        return new BCryptEncoder();
    }
}
