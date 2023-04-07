package io.wisoft.capstonedesign.config.bcrypt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BCryptEncoderTest {

    @Autowired EncryptHelper encryptHelper;

    @Test
    @DisplayName("비밀번호가 Bcrypt 암호화가 되었는지 확인")
    public void passwordIsHashBcrypt() throws Exception {
        //given -- 조건
        String password = "1234";

        //when -- 동작
        String result = encryptHelper.encrypt(password);

        //then -- 검증
        Assertions.assertThat(result.length()).isEqualTo(60);
        Assertions.assertThat(result.startsWith("$2a$10$")).isTrue();
    }
}