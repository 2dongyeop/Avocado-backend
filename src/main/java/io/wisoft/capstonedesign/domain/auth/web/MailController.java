package io.wisoft.capstonedesign.domain.auth.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.MailObject;
import io.wisoft.capstonedesign.domain.auth.application.EmailService;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "이메일 인증")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @SwaggerApi(summary = "이메일 인증 코드 전송", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/mail/certification-code")
    public ResponseEntity<String> sendCertificationCode(@RequestBody final MailObject mailObject) {
        final String code = emailService.sendCertificationCode(mailObject.email());
        return ResponseEntity.ok(code);
    }

    @PostMapping("/mail/certification-code2")
    public CompletableFuture<String> sendCertificationCode2(@RequestBody final MailObject mailObject) {
        return CompletableFuture.supplyAsync(() -> emailService.sendCertificationCode(mailObject.email()));
    }

    @SwaggerApi(summary = "이메일 인증", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/mail/certification-email")
    public ResponseEntity<String> certificateEmail(@RequestBody final CertificateMailRequest request) {
        emailService.certificateEmail(request);
        return ResponseEntity.ok("success");
    }


    @SwaggerApi(summary = "회원 비밀번호 찾기", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/mail/member/password")
    public ResponseEntity<String> resetMemberPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetMemberPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @SwaggerApi(summary = "의료진 비밀번호 찾기", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/mail/staff/password")
    public ResponseEntity<String> resetStaffPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetStaffPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }
}
