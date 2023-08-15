package io.wisoft.capstonedesign.global.config.aes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES implements Crypto {

    private final String ALGORITHM;
    private final SecretKey KEY;

    public AES(int keySize) throws Exception {
        ALGORITHM = "AES";
        this.KEY = generateKey(keySize);
    }

    @Override
    public String encrypt(final String plainText) throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KEY);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(encrypted));
    }

    @Override
    public String decrypt(final String cipherText) throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, KEY);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private SecretKey generateKey(final int keySize) throws Exception {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(keySize);

        return keyGenerator.generateKey();
    }
}
