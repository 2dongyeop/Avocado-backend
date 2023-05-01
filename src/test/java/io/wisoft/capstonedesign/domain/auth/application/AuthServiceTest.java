package io.wisoft.capstonedesign.domain.auth.application;

import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    EntityManager em;
    @Autowired AuthService authService;
    @Autowired
    MemberService memberService;
    @Autowired
    StaffService staffService;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    MailAuthenticationRepository mailAuthenticationRepository;

    @Test
    public void 회원_저장() throws Exception {
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
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
        final Long signUpId = authService.signUpMember(request);

        //then -- 검증
        final Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.nickname());
    }

    @Test
    public void 회원_이메일_중복_검증() throws Exception {

        //given -- 조건
        final CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        final CreateMemberRequest request2 = CreateMemberRequest.builder()
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
        //then -- 검증
        assertThrows(DuplicateMemberException.class, () -> {
            authService.signUpMember(request1);
            authService.signUpMember(request2);
        });
    }

    @Test
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건

        final CreateMemberRequest request1 = CreateMemberRequest.builder()
                .nickname("test1")
                .email("email1@naver.com")
                .password1("1111")
                .password2("1111")
                .phonenumber("0000")
                .code("ssss")
                .build();

        final CreateMemberRequest request2 = CreateMemberRequest.builder()
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
        //then -- 검증
        assertThrows(DuplicateMemberException.class, () -> {
            authService.signUpMember(request1);
            authService.signUpMember(request2);
        });
    }


    @Test
    public void member_login() throws Exception {
        //given -- 조건
        final CreateMemberRequest request = CreateMemberRequest.builder()
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
        final LoginRequest loginRequest = new LoginRequest(request.email(), request.password1());
        final String token = authService.loginMember(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void member_login_fail() throws Exception {
        //given -- 조건

        final CreateMemberRequest request = CreateMemberRequest.builder()
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
        //then -- 검증

        assertThrows(IllegalValueException.class, () -> {
            LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            String token = authService.loginMember(loginRequest);
        });
    }



    @Test
    public void member_createToken_isMatch() throws Exception {
        //given -- 조건
        final String password = "1234";
        final String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        final boolean result = encryptHelper.isMatch(password, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void member_createToken_isMatch_Fail() throws Exception {
        //given -- 조건
        final String password = "1234";
        final String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        final String failPassword = "1233";
        final boolean result = encryptHelper.isMatch(failPassword, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void 의료진가입() throws Exception {
        //given -- 조건

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
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
        final Long signUpId = authService.signUpStaff(request);

        //then -- 검증
        final Staff staff = staffService.findById(signUpId);
        Assertions.assertThat(staff.getName()).isEqualTo(request.name());
        Assertions.assertThat(staff.getEmail()).isEqualTo(request.email());
    }


    @Test
    public void 의료진중복검증() throws Exception {
        //given -- 조건

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);


        final CreateStaffRequest request1 = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        final CreateStaffRequest request2 = CreateStaffRequest.builder()
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
        //then -- 검증

        assertThrows(DuplicateStaffException.class, () -> {
            authService.signUpStaff(request1);
            authService.signUpStaff(request2);
        });
    }


    @Test
    public void staff_login() throws Exception {
        //given -- 조건

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = new CreateStaffRequest(
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

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = new CreateStaffRequest(
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

        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            LoginRequest loginRequest = new LoginRequest(request.email(), "fail-password");
            String token = authService.loginStaff(loginRequest);
        });
    }


    @Test
    public void staff_createToken_isMatch() throws Exception {
        //given -- 조건
        final String password = "1234";
        final String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        final boolean result = encryptHelper.isMatch(password, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void staff_createToken_isMatch_Fail() throws Exception {
        //given -- 조건
        final String password = "1234";
        final String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        final String failPassword = "1233";
        final boolean result = encryptHelper.isMatch(failPassword, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }

}