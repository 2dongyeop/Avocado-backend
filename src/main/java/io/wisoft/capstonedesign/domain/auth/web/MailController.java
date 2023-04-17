package io.wisoft.capstonedesign.domain.auth.web;

import io.wisoft.capstonedesign.domain.auth.web.dto.MailObject;
import io.wisoft.capstonedesign.domain.auth.application.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("/mail/certification")
    public ResponseEntity<String> certificateEmail(@RequestBody final MailObject mailObject) {
        emailService.sendCertificationCode(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/mail/member/password")
    public ResponseEntity<String> resetMemberPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetMemberPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/mail/staff/password")
    public ResponseEntity<String> resetStaffPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetStaffPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }
}
