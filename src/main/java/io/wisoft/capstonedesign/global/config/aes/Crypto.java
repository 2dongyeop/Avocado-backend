package io.wisoft.capstonedesign.global.config.aes;

public interface Crypto {
    String encrypt(final String plainText) throws Exception;

    String decrypt(final String cipherText) throws Exception;
}
