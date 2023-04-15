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

    @PostMapping("/mail/password")
    public ResponseEntity<String> resetPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetPassword(mailObject.getEmail());
        return ResponseEntity.ok().build();
    }
}
