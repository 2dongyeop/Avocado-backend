package io.wisoft.capstonedesign.global.config.aes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AESTest {

    private final String plainText = "plainText";

    @Test
    @DisplayName("평문을 암호화했을 때, AES128로 암호화된 결과값이 나와야 한다.")
    void encryptString() {

        // given

        // when
        final String encrypted = AES.encryptString(plainText);

        // then
        assertThat(encrypted).isNotEqualTo(plainText);
    }

    @Test
    @DisplayName("암호문을 평문으로 복호화하면, 암호화 전의 문자열과 동일해야 한다.")
    void decryptString() {

        // given
        // 암호화
        final String encrypted = AES.encryptString(plainText);

        // when
        final String decrypted = AES.decryptString(encrypted);

        // then
        assertThat(decrypted).isEqualTo(plainText);
    }
}