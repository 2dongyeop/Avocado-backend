package io.wisoft.capstonedesign.global.config.aes;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public final class AES {

    private static final String KEY = "3aq1b922pi106o35fe8025947de7cd71"; // 32byte 암호화 키
    private static final String ENCRYPTION_ALGORITHM = "AES";

    /**
     * 16byte 키 생성 메서드
     * 16byte 이상은 잘라서 다시 앞쪽부터 byte 단위로 XOR 연산 시행
     *
     * @param encryptionAlgorithm : 인코딩 방식
     * @return : 암호화 키
     */
    public static SecretKeySpec generateMySqlAesKey(final String key, final String encryptionAlgorithm) {

        try {
            final byte[] finalKey = new byte[16];
            int i = 0;

            for (byte b : key.getBytes(encryptionAlgorithm)) {
                finalKey[i++ % 16] ^= b;
            }

            return new SecretKeySpec(finalKey, ENCRYPTION_ALGORITHM);
        } catch (UnsupportedEncodingException e) {
            log.warn("encryptionAlgorithm:[{}] can not generate AES Key", encryptionAlgorithm);
            throw new RuntimeException(e);
        }
    }

    /**
     * 평문을 AES 알고리즘으로 암호화
     *
     * @param plainText : 암호화할 평문
     * @return String : 암호화된 문자
     * @throws Exception : 발생하는 예외
     */
    public static String encryptString(final String plainText) {

        try {
            final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateMySqlAesKey(KEY, StandardCharsets.UTF_8.toString()));

            final byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            final String encryptedString = Hex.encodeHexString(encrypted);
            return encryptedString;

        } catch (Exception e) {
            log.warn("plainText:[{}] can not be encrypt", plainText);
        }
        return Strings.EMPTY;
    }

    /**
     * 암호화된 문자를 평문으로 복호화
     *
     * @param encryptedText : 복호화 할 암호문
     * @return String : 복호화된 평문
     */
    public static String decryptString(final String encryptedText) {

        try {
            final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateMySqlAesKey(KEY, StandardCharsets.UTF_8.toString()));

            final byte[] original = cipher.doFinal(Hex.decodeHex(encryptedText.toCharArray()));

            final String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;

        } catch (Exception e) {
            log.warn("encryptedText:[{}] can not be decrypt", encryptedText);
        }
        return Strings.EMPTY;
    }
}
