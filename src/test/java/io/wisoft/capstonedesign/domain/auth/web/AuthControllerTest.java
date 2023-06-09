package io.wisoft.capstonedesign.domain.auth.web;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.capstonedesign.setting.data.HospitalTestData.getDefaultHospital;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired EntityManager em;
    @Autowired AuthController authController;
    @Autowired MailController mailController;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;

    @Test
    public void signupMember_success() throws Exception {
        //given -- 조건

        sendCertificationCodeAndCertificateCode("test-email@naver.com");

        final CreateMemberRequest request = getCreateMemberRequest("test-email@naver.com", "password1");

        //when -- 동작
        final CreateMemberResponse response = authController.signupMember(request);

        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
    }


    @Test
    public void signupMember_fail_password_isNotMatch() throws Exception {
        //given -- 조건
        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);

        final CreateMemberRequest request = getCreateMemberRequest(email, "password2");

        //when -- 동작
        //then -- 검증

        assertThrows(IllegalValueException.class, () -> {
            authController.signupMember(request);
        });
    }

    @Test
    public void loginMember_success() throws Exception {
        //given -- 조건
        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);

        final CreateMemberRequest request = getCreateMemberRequest(email, "password1");

        final CreateMemberResponse response = authController.signupMember(request);

        final LoginRequest loginRequest = new LoginRequest(email, "password1");

        //when -- 동작
        final ResponseEntity<TokenResponse> tokenResponseResponseEntity = authController.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(tokenResponseResponseEntity.getBody().accessToken()).isNotNull();
    }

    @Test
    public void loginMember_fail_email_NotFound() throws Exception {
        //given -- 조건
        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);

        //회원가입 요청
        final CreateMemberRequest request = getCreateMemberRequest(email, "password1");

        //회원가입
        final CreateMemberResponse response = authController.signupMember(request);

        //로그인 요청
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

        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);


        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        final CreateStaffRequest request = getCreateStaffRequest(hospital.getId(), email, "password");

        //when -- 동작
        final CreateStaffResponse response = authController.signupStaff(request);

        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
    }



    @Test
    public void signUpStaff_fail_hospital_notFound() throws Exception {
        //given -- 조건
        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);

        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        final CreateStaffRequest request = getCreateStaffRequest(10000L, email, "password");

        //when -- 동작
        //then -- 검증
        assertThrows(NullHospitalException.class, () -> {
            authController.signupStaff(request);
        });
    }

    @Test
    public void signUpStaff_fail_password_isNotMatch() throws Exception {
        //given -- 조건

        final String email = "test-email@naver.com";

        //인증 코드 전송
        sendCertificationCodeAndCertificateCode(email);


        final Hospital hospital = Hospital.builder()
                .name("name")
                .number("number")
                .address("address")
                .operatingTime("time")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = getCreateStaffRequest(10000L, email, "password1231232132");

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            authController.signupStaff(request);
        });
    }

    private void sendCertificationCodeAndCertificateCode(final String email) {
        //인증 코드 전송
        final MailObject mailObject = new MailObject(email);
        final String code = mailController.sendCertificationCode(mailObject).getBody();

        //이메일 인증
        final CertificateMailRequest emailRequest = new CertificateMailRequest(email, code);
        mailController.certificateEmail(emailRequest);
    }

    private CreateMemberRequest getCreateMemberRequest(final String mail, final String password1) {
        return CreateMemberRequest.builder()
                .nickname("test")
                .email(mail)
                .password1("password1")
                .password2(password1)
                .phonenumber("phonenumber")
                .build();
    }

    private CreateStaffRequest getCreateStaffRequest(final Long hospitalId, final String email, final String password) {
        return CreateStaffRequest.builder()
                .hospitalId(hospitalId)
                .name("name")
                .email(email)
                .password1("password")
                .password2(password)
                .licensePath("license")
                .dept("DENTAL")
                .build();
    }
}