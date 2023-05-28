package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.capstonedesign.setting.data.HospitalTestData.getDefaultHospital;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

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
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건
        final String email1 = "email@naver.com";
        final String email2 = "email111111@naver.com";

        sendCertificationCodeAndCertificationCode(email1);
        sendCertificationCodeAndCertificationCode(email2);

        //회원가입 요청
        final CreateMemberRequest request1 = getCreateMemberRequest(email1);
        final CreateMemberRequest request2 = getCreateMemberRequest(email2);

        //when -- 동작
        //then -- 검증
        assertThrows(DuplicateMemberException.class, () -> {
            authService.signUpMember(request1);
            authService.signUpMember(request2);
        });
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
        final String token = authService.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void member_login_fail() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        sendCertificationCodeAndCertificationCode(email);

        //회원가입 요청
        final CreateMemberRequest request = getCreateMemberRequest(email);

        //회원가입
        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        //then -- 검증

        assertThrows(IllegalValueException.class, () -> {
            LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            String token = authService.loginMember(loginRequest);
        });
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
        final CreateStaffRequest request = getCreateStaffRequest(email, hospital);

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
        final CreateStaffRequest request = getCreateStaffRequest(email, hospital);

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        final LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        final String token = authService.loginStaff(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
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
        final CreateStaffRequest request = getCreateStaffRequest(email, hospital);

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            String token = authService.loginStaff(loginRequest);
        });
    }

    private void sendCertificationCodeAndCertificationCode(final String email) {
        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);
    }

    private static CreateMemberRequest getCreateMemberRequest(final String email) {
        return CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();
    }

    private static CreateStaffRequest getCreateStaffRequest(final String email, final Hospital hospital) {
        return CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();
    }

}