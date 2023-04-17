package io.wisoft.capstonedesign.domain.mail.web;

import io.wisoft.capstonedesign.domain.mail.persistence.MailObject;
import io.wisoft.capstonedesign.domain.mail.application.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MailController {

    @Autowired public EmailService emailService;

    @PostMapping("/mail/certification")
    public ResponseEntity<String> certificateEmail(@RequestBody final MailObject mailObject) {
        emailService.sendCertificationCode(mailObject.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mail/member/password")
    public ResponseEntity<String> resetMemberPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetMemberPassword(mailObject.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mail/staff/password")
    public ResponseEntity<String> resetStaffPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetStaffPassword(mailObject.getEmail());
        return ResponseEntity.ok().build();
    }
}
