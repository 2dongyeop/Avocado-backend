package io.wisoft.capstonedesign.global.config.aes;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    private final String ALGORITHM = "AES";
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    final String plainText = "test123";

    @BeforeEach
    void init() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        final SecretKey KEY = generateKey();

        encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, KEY);

        decryptCipher = Cipher.getInstance(ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, KEY);
    }


    @Test
    @DisplayName("평문을 AES 알고리즘으로 암호화하면 끝이 ==로 끝나도록 암호화된다.")
    void encrypt() throws IllegalBlockSizeException, BadPaddingException {

        // given

        // when - 암호화
        final byte[] encrypted = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        final String cipherText = new String(Base64.getEncoder().encode(encrypted));

        // then
        Assertions.assertThat(cipherText).isNotEqualTo(plainText);
        Assertions.assertThat(cipherText).endsWith("==");
    }

    @Test
    @DisplayName("AES 알고리즘으로 암호화된 글자를 복호화하면 평문과 원문이 같아야 한다.")
    void decrypt() throws IllegalBlockSizeException, BadPaddingException {

        // given

        // 암호화
        final byte[] encrypted = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        final String cipherText = new String(Base64.getEncoder().encode(encrypted));

        // when - 복호화
        byte[] decrypted = decryptCipher.doFinal(Base64.getDecoder().decode(cipherText));
        final String decryptedText = new String(decrypted, StandardCharsets.UTF_8);

        // then
        Assertions.assertThat(plainText).isEqualTo(decryptedText);
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);

        return keyGenerator.generateKey();
    }
}