package io.wisoft.capstonedesign.config.bcrypt;

public interface EncryptHelper {
    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}
