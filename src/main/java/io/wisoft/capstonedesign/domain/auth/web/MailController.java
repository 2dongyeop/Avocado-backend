package io.wisoft.capstonedesign.domain.auth.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.auth.application.EmailService;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.MailObject;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "이메일 인증")
@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    @Qualifier("asyncExecutor")
    private final ThreadPoolTaskExecutor executor;

    @SwaggerApi(summary = "이메일 인증 코드 전송", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/certification-code")
    public ResponseEntity<String> sendCertificationCode(@RequestBody final MailObject mailObject) {

        log.debug("MailObject[{}]", mailObject);

        final CompletableFuture<String> future = CompletableFuture.supplyAsync(
                        () -> emailService.sendCertificationCode(mailObject.email()), executor);

        final String code = future.join();
        return ResponseEntity.ok(code);
    }


    @SwaggerApi(summary = "이메일 인증", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/certification-email")
    public ResponseEntity<String> certificateEmail(@RequestBody final CertificateMailRequest request) {

        log.debug("CertificateMailRequest[{}]", request);

        emailService.certificateEmail(request);
        return ResponseEntity.ok("success");
    }


    @SwaggerApi(summary = "회원 비밀번호 찾기", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/member/password")
    public ResponseEntity<String> resetMemberPassword(@RequestBody final MailObject mailObject) {

        log.debug("MailObject[{}]", mailObject);

        emailService.sendResetMemberPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @SwaggerApi(summary = "의료진 비밀번호 찾기", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/staff/password")
    public ResponseEntity<String> resetStaffPassword(@RequestBody final MailObject mailObject) {

        log.debug("MailObject[{}]", mailObject);

        emailService.sendResetStaffPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }
}
