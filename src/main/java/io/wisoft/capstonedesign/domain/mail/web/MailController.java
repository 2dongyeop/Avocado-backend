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

    @PostMapping("/mail/send")
    public ResponseEntity<String> createMail(@RequestBody MailObject mailObject) {
        emailService.sendSimpleMessage(mailObject.getTo(), mailObject.getSubject(), mailObject.getText());
        return ResponseEntity.ok("ok");
    }
}
