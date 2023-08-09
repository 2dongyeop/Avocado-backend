package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.wisoft.capstonedesign.setting.data.HospitalTestData.getDefaultHospital;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest extends ServiceTest {

    @Autowired EntityManager em;
    @Autowired AuthService authService;
    @Autowired EmailServiceImpl emailService;
    @Autowired MemberService memberService;
    @Autowired StaffService staffService;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;

    @Test
    public void 회원가입() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        sendCertificationCodeAndCertificationCode(email);

        //회원가입 요청
        final CreateMemberRequest request = getCreateMemberRequest(email);

        //when -- 동작
        final Long signUpId = authService.signUpMember(request);

        //then -- 검증
        final Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.nickname());
    }


    @Test
    public void member_login() throws Exception {
        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        sendCertificationCodeAndCertificationCode(email);

        //회원가입 요청
        final CreateMemberRequest request = getCreateMemberRequest(email);

        //회원가입
        final Long signUpId = authService.signUpMember(request);


        //when -- 동작
        final LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        final TokenResponse tokenResponse = authService.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(tokenResponse.accessToken()).isNotNull();
        Assertions.assertThat(tokenResponse.refreshToken()).isNotNull();
    }


    @Test
    public void 의료진가입() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        sendCertificationCodeAndCertificationCode(email);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = getCreateStaffRequest(email);

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //then -- 검증
        final Staff staff = staffService.findById(signUpId);
        Assertions.assertThat(staff.getName()).isEqualTo(request.name());
        Assertions.assertThat(staff.getEmail()).isEqualTo(request.email());
    }


    @Test
    public void staff_login() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        sendCertificationCodeAndCertificationCode(email);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = getCreateStaffRequest(email);

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        final LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        final TokenResponse tokenResponse = authService.loginStaff(loginRequest);

        //then -- 검증
        Assertions.assertThat(tokenResponse.accessToken()).isNotNull();
        Assertions.assertThat(tokenResponse.refreshToken()).isNotNull();
    }


    @Test
    public void staff_login_fail() throws Exception {
        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        sendCertificationCodeAndCertificationCode(email);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = getCreateStaffRequest(email);

        //when -- 동작
        authService.signUpStaff(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            final LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            authService.loginStaff(loginRequest);
        });
    }

    private void sendCertificationCodeAndCertificationCode(final String email) {
        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);
    }

    private CreateMemberRequest getCreateMemberRequest(final String email) {
        return CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();
    }

    private CreateStaffRequest getCreateStaffRequest(final String email) {
        return CreateStaffRequest.builder()
                .hospitalName("을지대학병원")
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();
    }

}