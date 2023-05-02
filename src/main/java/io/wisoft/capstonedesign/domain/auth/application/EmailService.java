package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;

public interface EmailService {
    String sendCertificationCode(final String email);
    void sendResetMemberPassword(final String to);
    void sendResetStaffPassword(final String to);
    void certificateEmail(final CertificateMailRequest request);
}
