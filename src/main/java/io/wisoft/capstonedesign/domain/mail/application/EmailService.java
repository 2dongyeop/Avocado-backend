package io.wisoft.capstonedesign.domain.mail.application;

public interface EmailService {
    void sendCertificationCode(final String email);
    void sendResetMemberPassword(final String to);
    void sendResetStaffPassword(final String to);
}
