package io.wisoft.capstonedesign.domain.mail.application;

public interface EmailService {
    void sendCertificationCode(final String email);
    void sendResetPassword(final String to);
}
