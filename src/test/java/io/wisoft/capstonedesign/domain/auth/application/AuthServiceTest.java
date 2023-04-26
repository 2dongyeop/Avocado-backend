package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired EntityManager em;
    @Autowired AuthService authService;
    @Autowired MemberService memberService;
    @Autowired StaffService staffService;
    @Autowired EncryptHelper encryptHelper;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;

    @Test
    public void 회원_저장() throws Exception {
        //given -- 조건
        CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email@naver.com")
                .code("ssss")
                .build());

        //when -- 동작
        Long signUpId = authService.signUpMember(request);

        //then -- 검증
        Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.nickname());
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_이메일_중복_검증() throws Exception {

        //given -- 조건
        CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        CreateMemberRequest request2 = CreateMemberRequest.builder()
                .nickname("test2")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email@naver.com")
                .code("ssss")
                .build());

        //when -- 동작
        authService.signUpMember(request1);
        authService.signUpMember(request2);

        //then -- 검증
        fail("회원의 이메일이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건

        CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email1@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        CreateMemberRequest request2 = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email2@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1@naver.com")
                .code("ssss")
                .build());
        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email2@naver.com")
                .code("ssss")
                .build());

        //when -- 동작
        authService.signUpMember(request1);
        authService.signUpMember(request2);

        //then -- 검증
        fail("회원의 닉네임이 중복되어 예외가 발생해야 한다.");
    }


    @Test
    public void member_login() throws Exception {
        //given -- 조건
        CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email@naver.com")
                .code("ssss")
                .build());
        authService.signUpMember(request);

        //when -- 동작
        LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        String token = authService.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void member_login_fail() throws Exception {
        //given -- 조건

        CreateMemberRequest request = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email@naver.com")
                .code("ssss")
                .build());
        authService.signUpMember(request);

        //when -- 동작
        LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
        String token = authService.loginMember(loginRequest);

        //then -- 검증
        fail("비밀번호가 일치하지 않아 예외가 발생해야 한다.");
    }



    @Test
    public void member_createToken_isMatch() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        boolean result = encryptHelper.isMatch(password, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void member_createToken_isMatch_Fail() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        String failPassword = "1233";
        boolean result = encryptHelper.isMatch(failPassword, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void 의료진가입() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        //when -- 동작
        Long signUpId = authService.signUpStaff(request);

        //then -- 검증
        Staff staff = staffService.findById(signUpId);
        Assertions.assertThat(staff.getName()).isEqualTo(request.name());
        Assertions.assertThat(staff.getEmail()).isEqualTo(request.email());
    }


    @Test(expected = DuplicateStaffException.class)
    public void 의료진중복검증() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);


        CreateStaffRequest request1 = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        CreateStaffRequest request2 = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff2")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        //when -- 동작
        authService.signUpStaff(request1);
        authService.signUpStaff(request2);

        //then -- 검증
        fail("의료진의 이메일이 중복되어 예외가 발생해야 한다.");
    }


    @Test
    public void staff_login() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request = new CreateStaffRequest(
                hospital.getId(),
                "staff1",
                "email1",
                "pw1",
                "pw2",
                "path1",
                "DENTAL",
                "ssss"
        );
        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        String token = authService.loginStaff(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }


    @Test(expected = IllegalValueException.class)
    public void staff_login_fail() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request = new CreateStaffRequest(
                hospital.getId(),
                "staff1",
                "email1",
                "pw1",
                "pw2",
                "path1",
                "DENTAL",
                "ssss"
        );
        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
        String token = authService.loginStaff(loginRequest);

        //then -- 검증
        fail("비밀번호가 일치하지 않아 예외가 발생해야 한다.");
    }


    @Test
    public void staff_createToken_isMatch() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        boolean result = encryptHelper.isMatch(password, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void staff_createToken_isMatch_Fail() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        String failPassword = "1233";
        boolean result = encryptHelper.isMatch(failPassword, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }

}