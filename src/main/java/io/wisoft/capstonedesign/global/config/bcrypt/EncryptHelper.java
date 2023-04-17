package io.wisoft.capstonedesign.global.config.bcrypt;

public interface EncryptHelper {
    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}
