package io.wisoft.capstonedesign.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MailController {

    @Autowired
    public EmailService emailService;

    private static final Map<String, Map<String, String>> labels;

    static {
        labels = new HashMap<>();

        //Simple email
        Map<String, String> props = new HashMap<>();
        props.put("headerText", "Send Simple Email");
        props.put("messageLabel", "Message");
        props.put("additionalInfo", "");
        labels.put("send", props);
    }

    @PostMapping("/mail/send")
    public ResponseEntity<String> createMail(MailObject mailObject) {
        emailService.sendSimpleMessage(mailObject.getTo(), mailObject.getSubject(), mailObject.getText());
        return ResponseEntity.ok("ok");
    }
}
