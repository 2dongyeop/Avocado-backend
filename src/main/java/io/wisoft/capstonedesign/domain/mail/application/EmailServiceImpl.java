package io.wisoft.capstonedesign.domain.mail.application;

import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service("EmailService")
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String AVOCADO_ADDRESS;

    @Autowired private JavaMailSender emailSender;
    @Autowired private MemberRepository memberRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private EncryptHelper encryptHelper;

    private static final String EMAIL_CERTIFICATION_SUBJECT = "AVOCADO 이메일 인증 코드입니다.";
    private static final String PASSWORD_RESET_SUBJECT = "AVOCADO 임시 비밀번호입니다.";

    @Async
    public void sendCertificationCode(final String to) {
        sendEmail(to, EMAIL_CERTIFICATION_SUBJECT);
        log.info(to + "으로 인증 코드를 발송합니다.");
    }

    @Async
    @Transactional
    public void sendResetMemberPassword(final String to) {
        String temporaryPassword = sendEmail(to, PASSWORD_RESET_SUBJECT);

        Member member = memberRepository.findByEmail(to).orElseThrow(NullMemberException::new);
        member.updatePassword(encryptHelper.encrypt(temporaryPassword));

        log.info(to + "으로 임시 비밀번호를 발급합니다.");
    }

    @Async
    @Transactional
    public void sendResetStaffPassword(final String to) {
        String temporaryPassword = sendEmail(to, PASSWORD_RESET_SUBJECT);

        Staff staff = staffRepository.findByEmail(to).orElseThrow(NullStaffException::new);
        staff.updatePassword(encryptHelper.encrypt(temporaryPassword));

        log.info(to + "으로 임시 비밀번호를 발급합니다.");
    }

    private String sendEmail(final String to, final String subject) {
        String code = createCertificationCode();
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(AVOCADO_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(code);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
        
        return code;
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
