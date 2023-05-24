package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.application.EmailServiceImpl;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPasswordRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired AuthService authService;
    @Autowired MemberService memberService;
    @Autowired EmailServiceImpl emailService;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;


    @Test
    public void 회원_단건_조회() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        Member member = memberService.findById(signUpId);

        //then -- 검증
        Assertions.assertThat(member).isNotNull();
        Assertions.assertThat(member.getNickname()).isEqualTo("test1");
    }

    @Test
    public void 회원_단건_조회_실패() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullMemberException.class, () -> {
            Member getMember = memberService.findById(100L);
        });
    }

    @Test
    public void 회원_비밀번호_수정() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        final Member getMember = memberService.findById(signUpId);

        final UpdateMemberPasswordRequest request2 = new UpdateMemberPasswordRequest("1111", "2222");
        memberService.updatePassword(getMember.getId(), request2);

        //then -- 검증
        final Member updatedMember = memberService.findById(signUpId);
        Assertions.assertThat(getMember.getPassword()).isEqualTo(updatedMember.getPassword());
    }

    @Test
    public void 회원_비밀번호_수정_실패() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        final Member getMember = memberService.findById(signUpId);
        final UpdateMemberPasswordRequest request2 = new UpdateMemberPasswordRequest("1232132131", "2222");


        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            memberService.updatePassword(getMember.getId(), request2);
        });
    }


    @Test
    public void updateMember_success() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        final String photoPath = "photoPath";
        final String nickname = "";
        memberService.updateMember(signUpId, photoPath, nickname);

        //then -- 검증
        final Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getMemberPhotoPath()).isEqualTo(photoPath);
        Assertions.assertThat(member.getNickname()).isNotEqualTo(nickname);
    }

    @Test
    public void 회원_탈퇴() throws Exception {
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

        final Long signUpId = authService.signUpMember(request);

        //when -- 동작
        memberService.deleteMember(signUpId);

        //then -- 검증
        assertThrows(NullMemberException.class, () -> {
            Member member1 = memberService.findById(signUpId);
        });
    }
}