package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EmailServiceImplTest {

    @Autowired EmailService emailService;
    @Autowired MemberRepository memberRepository;
    @Autowired StaffRepository staffRepository;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;
    @Autowired EncryptHelper encryptHelper;

    @Test
    public void sendCertificationCode() throws Exception {
        //given -- 조건
        final String authenticateCode = createCertificationCode();

        //when -- 동작
        final MailAuthentication mail = mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email(null)
                        .code(authenticateCode)
                        .build());

        //then -- 검증
        final MailAuthentication getMail= mailAuthenticationRepository.findById(mail.getId()).orElseThrow();
        Assertions.assertThat(mail.getCode()).isEqualTo(getMail.getCode());
    }


    @Test
    public void createCertificationCodeTest() throws Exception {
        //given -- 조건

        //when -- 동작
        final String result = createCertificationCode();

        //then -- 검증
        Assertions.assertThat(result.length()).isEqualTo(8);
    }

    private String createCertificationCode() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Random random = new Random();

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
        final String code = stringBuilder.toString();
        return code;
    }
}