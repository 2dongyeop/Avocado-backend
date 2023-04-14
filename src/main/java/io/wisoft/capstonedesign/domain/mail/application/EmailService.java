package io.wisoft.capstonedesign.domain.mail.application;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
