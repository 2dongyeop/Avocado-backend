package io.wisoft.capstonedesign.domain.auth.web;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired EntityManager em;
    @Autowired AuthController authController;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;
    @Autowired MailController mailController;

    @Test
    public void signupMember_success() throws Exception {
        //given -- 조건

        String email = "test-email@naver.com";

        //인증 코드 전송
        MailObject mailObject = new MailObject(email);
        String code = mailController.sendCertificationCode(mailObject).getBody();

        //이메일 인증
        CertificateMailRequest emailRequest = new CertificateMailRequest(email, code);
        mailController.certificateEmail(emailRequest);


        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email(email)
                .password1("password1")
                .password2("password1")
                .phonenumber("phonenumber")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());

        //when -- 동작
        final CreateMemberResponse response = authController.signupMember(request);

        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
    }

    @Test
    public void signupMember_fail_password_isNotMatch() throws Exception {
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email("email@naver.com")
                .password1("password1")
                .password2("password2")
                .phonenumber("phonenumber")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());

        //when -- 동작
        //then -- 검증

        assertThrows(IllegalValueException.class, () -> {
            authController.signupMember(request);
        });
    }

    @Test
    public void signupMember_fail_code_isNotMatch() throws Exception {
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email("email@naver.com")
                .password1("password1")
                .password2("password1")
                .phonenumber("phonenumber")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code("fail-code")
                .isVerified(false)
                .build());

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            CreateMemberResponse response = authController.signupMember(request);
        });
    }


    @Test
    public void loginMember_success() throws Exception {
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email("email@naver.com")
                .password1("password1")
                .password2("password1")
                .phonenumber("phonenumber")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());
        authController.signupMember(request);

        final LoginRequest loginRequest = new LoginRequest("email@naver.com", "password1");

        //when -- 동작
        final ResponseEntity<TokenResponse> tokenResponseResponseEntity = authController.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(tokenResponseResponseEntity.getBody().accessToken()).isNotNull();
    }

    @Test
    public void loginMember_fail_password_isNotMatch() throws Exception {
        //given -- 조건
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email("email@naver.com")
                .password1("password1")
                .password2("password1")
                .phonenumber("phonenumber")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());
        authController.signupMember(request);

        final LoginRequest loginRequest = new LoginRequest("email@naver.com", "fail-password");


        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            authController.loginMember(loginRequest);
        });
    }

    @Test
    public void loginMember_fail_email_NotFound() throws Exception {
        //given -- 조건
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test")
                .email("email@naver.com")
                .password1("password1")
                .password2("password1")
                .phonenumber("phonenumber")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());
        authController.signupMember(request);

        final LoginRequest loginRequest = new LoginRequest("fail-email@naver.com", "password1");


        //when -- 동작
        //then -- 검증
        assertThrows(NullMemberException.class, () -> {
            authController.loginMember(loginRequest);
        });
    }

    @Test
    public void signUpStaff_success() throws Exception {
        //given -- 조건

        final Hospital hospital = Hospital.builder()
                .name("name")
                .number("number")
                .address("address")
                .operatingTime("time")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("name")
                .email("email@naver.com")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());

        //when -- 동작
        final CreateStaffResponse response = authController.signupStaff(request);

        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
    }

    @Test
    public void signUpStaff_fail_hospital_notFound() throws Exception {
        //given -- 조건
        final Hospital hospital = Hospital.builder()
                .name("name")
                .number("number")
                .address("address")
                .operatingTime("time")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(100L)
                .name("name")
                .email("email@naver.com")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());

        //when -- 동작
        //then -- 검증
        assertThrows(NullHospitalException.class, () -> {
            authController.signupStaff(request);
        });
    }

    @Test
    public void signUpStaff_fail_password_isNotMatch() throws Exception {
        //given -- 조건
        final Hospital hospital = Hospital.builder()
                .name("name")
                .number("number")
                .address("address")
                .operatingTime("time")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("name")
                .email("email@naver.com")
                .password1("password11111")
                .password2("password22222")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final String code = "ssss";

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code(code)
                .isVerified(false)
                .build());

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            authController.signupStaff(request);
        });
    }

    @Test
    public void signUpStaff_fail_code_isNotMatch() throws Exception {
        //given -- 조건
        final Hospital hospital = Hospital.builder()
                .name("name")
                .number("number")
                .address("address")
                .operatingTime("time")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(100L)
                .name("name")
                .email("email@naver.com")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final String code = "ssss";


        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email(request.email())
                .code("not-match-code")
                .isVerified(false)
                .build());

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            authController.signupStaff(request);
        });
    }
}