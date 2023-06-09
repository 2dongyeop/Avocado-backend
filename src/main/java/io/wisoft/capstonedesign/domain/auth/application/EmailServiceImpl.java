package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.DBMailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateEmailException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("EmailService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String AVOCADO_ADDRESS;
    private final int EXPIRED_TIME = 3 * 60;

    private final JavaMailSender emailSender;
    private final EncryptHelper encryptHelper;
    private final StaffRepository staffRepository;
    private final MemberRepository memberRepository;
    private final StringRedisTemplate redisTemplate;
    private final MailAuthenticationRepository mailAuthenticationRepository;

    private static final String EMAIL_CERTIFICATION_SUBJECT = "🥑 AVOCADO 이메일 인증 코드입니다.";
    private static final String PASSWORD_RESET_SUBJECT = "🥑 AVOCADO 임시 비밀번호입니다.";

    @Async
    public String sendCertificationCode(final String to) {
        validateDuplicateMemberOrStaff(to);
        final String authenticateCode = sendEmail(to, EMAIL_CERTIFICATION_SUBJECT);

        redisTemplate.opsForValue().set(
                to,
                authenticateCode,
                EXPIRED_TIME,
                TimeUnit.SECONDS
        );
        log.info("redis :  " + to + " 를 3분간 저장합니다.");

        log.info(to + "으로 인증 코드를 발송합니다.");
        return authenticateCode;
    }

    private void validateDuplicateMemberOrStaff(final String to) {
        final Optional<Member> member = memberRepository.findByEmail(to);
        final Optional<Staff> staff = staffRepository.findByEmail(to);

        if (member.isPresent() || staff.isPresent()) {
            log.error("일치하는 이메일이 존재해 이메일 인증에 실패하였습니다.");
            throw new DuplicateEmailException("일치하는 이메일이 존재해 이메일 인증에 실패하였습니다.", ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void certificateEmail(final CertificateMailRequest request) {

        //메일 정보 조회
        final String codeByRedis = getRedisValue(request.email());

        //메일 정보 검증
        validateBeforeCertificateEmail(codeByRedis, request);

        //Redis에서 메일 정보 삭제
        redisTemplate.delete(request.email());

        //인증된 이메일 목록으로 DB에 저장 - 회원가입 성공시 삭제
        mailAuthenticationRepository.save(
                DBMailAuthentication.builder()
                        .email(request.email())
                        .isVerified(true)
                        .build()
        );
    }

    private String getRedisValue(final String key) {
        final String code = redisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new NotFoundException("해당 이메일에 대한 응답코드 송신 기록이 없습니다.");
        }
        return code;
    }

    private void validateBeforeCertificateEmail(final String code, final CertificateMailRequest request) {
        if (request.code() == (code)) {
            throw new IllegalValueException("인증 코드가 달라 인증에 실패하였습니다.", ErrorCode.ILLEGAL_CODE);
        }
    }

    @Async
    @Transactional
    public void sendResetMemberPassword(final String to) {
        final Member member = memberRepository.findByEmail(to).orElseThrow(NotFoundException::new);
        final String temporaryPassword = sendEmail(to, PASSWORD_RESET_SUBJECT);

        member.updatePassword(encryptHelper.encrypt(temporaryPassword));

        log.info(to + "으로 임시 비밀번호를 발급합니다.");
    }

    @Async
    @Transactional
    public void sendResetStaffPassword(final String to) {
        final Staff staff = staffRepository.findByEmail(to).orElseThrow(NotFoundException::new);
        final String temporaryPassword = sendEmail(to, PASSWORD_RESET_SUBJECT);

        staff.updatePassword(encryptHelper.encrypt(temporaryPassword));

        log.info(to + "으로 임시 비밀번호를 발급합니다.");
    }

    private String sendEmail(final String to, final String subject) {
        final String code = createCertificationCode();

        try {
            final SimpleMailMessage message = createMessage(to, subject, code);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }

        return code;
    }

    @NotNull
    private SimpleMailMessage createMessage(final String to, final String subject, final String code) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(AVOCADO_ADDRESS);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(code);
        return message;
    }

    private String createCertificationCode() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Random random = new Random();

        final String code = randomProcess(stringBuilder, random);
        log.info("code : " + code);
        return code;
    }

    @NotNull
    private String randomProcess(final StringBuilder stringBuilder, final Random random) {
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            final int index = random.nextInt(3); // 0~2 까지 랜덤

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
