package io.wisoft.capstonedesign.domain.auth.application;

public interface EmailService {
    void sendCertificationCode(final String email);
    void sendResetMemberPassword(final String to);
    void sendResetStaffPassword(final String to);
}
