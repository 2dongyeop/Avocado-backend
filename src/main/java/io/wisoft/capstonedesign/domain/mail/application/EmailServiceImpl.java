package io.wisoft.capstonedesign.domain.mail.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service("EmailService")
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String AVOCADO_ADDRESS;

    @Autowired
    private JavaMailSender emailSender;

    private static final String EMAIL_CERTIFICATION_SUBJECT = "AVOCADO 이메일 인증 코드입니다.";
    private static final String PASSWORD_RESET_SUBJECT = "AVOCADO 임시 비밀번호입니다.";

    @Async
    public void sendCertificationCode(final String to) {
        sendEmail(to, EMAIL_CERTIFICATION_SUBJECT);
        log.info(to + "으로 인증 코드를 발송합니다.");
    }

    @Async
    public void sendResetPassword(final String to) {
        sendEmail(to, PASSWORD_RESET_SUBJECT);
        log.info(to + "으로 임시 비밀번호를 발급합니다.");
    }

    private void sendEmail(final String to, final String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(AVOCADO_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(createCertificationCode());

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    private String createCertificationCode() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = random.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                case 0 -> stringBuilder.append((char) ((int) (random.nextInt(26)) + 97));

                //  A~Z
                case 1 -> stringBuilder.append((char) ((int) (random.nextInt(26)) + 65));

                // 0~9
                case 2 -> stringBuilder.append((random.nextInt(10)));
            }
        }
        String code = stringBuilder.toString();
        log.info("code : " + code);
        return code;
    }
}
