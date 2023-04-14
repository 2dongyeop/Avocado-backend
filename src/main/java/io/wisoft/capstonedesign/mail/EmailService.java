package io.wisoft.capstonedesign.mail;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
