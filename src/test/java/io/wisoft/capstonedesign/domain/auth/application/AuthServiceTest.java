package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //회원가입 요청
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

        //when -- 동작
        final Long signUpId = authService.signUpMember(request);

        //then -- 검증
        final Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.nickname());
    }

    @Test
    public void 회원_이메일_중복_검증() throws Exception {

        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //회원가입 요청
        final CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

        final CreateMemberRequest request2 = CreateMemberRequest.builder()
                .nickname("test2")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

        //when -- 동작
        //then -- 검증
        assertThrows(DuplicateMemberException.class, () -> {
            authService.signUpMember(request1);
            authService.signUpMember(request2);
        });
    }

    @Test
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건
        final String email1 = "email@naver.com";
        final String email2 = "email@naver.com + 2";

        //이메일 인증 코드 보내기
        final String code1 = emailService.sendCertificationCode(email1);
        final String code2 = emailService.sendCertificationCode(email2);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email1, code1);
        emailService.certificateEmail(mailRequest);

        final CertificateMailRequest mailRequest2 = new CertificateMailRequest(email2, code2);
        emailService.certificateEmail(mailRequest2);

        //회원가입 요청
        final CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email1)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

        final CreateMemberRequest request2 = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email2)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

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
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //회원가입 요청
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

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
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //회원가입 요청
        final CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email(email)
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .build();

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
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //then -- 검증
        final Staff staff = staffService.findById(signUpId);
        Assertions.assertThat(staff.getName()).isEqualTo(request.name());
        Assertions.assertThat(staff.getEmail()).isEqualTo(request.email());
    }


    @Test
    public void 의료진중복검증() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request1 = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final CreateStaffRequest request2 = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        //when -- 동작
        //then -- 검증

        assertThrows(DuplicateStaffException.class, () -> {
            authService.signUpStaff(request1);
            authService.signUpStaff(request2);
        });
    }


    @Test
    public void staff_login() throws Exception {
        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

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
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            String token = authService.loginStaff(loginRequest);
        });
    }

}