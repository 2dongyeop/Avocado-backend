package io.wisoft.capstonedesign.domain.member.service;

import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
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
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired EncryptHelper encryptHelper;

    @Test
    public void 회원_저장() throws Exception {
        //given -- 조건
        CreateMemberRequest request = new CreateMemberRequest("test1", "email@naver.com", "1111", "1111", "0000");

        //when -- 동작
        Long signUpId = memberService.signUp(request);

        //then -- 검증
        Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.getNickname());
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_이메일_중복_검증() throws Exception {

        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "ldy_1204@naver.com", "1111", "1111", "0000");
        CreateMemberRequest request2 = new CreateMemberRequest("test2", "ldy_1204@naver.com", "1111", "1111", "0000");

        //when -- 동작
        memberService.signUp(request1);
        memberService.signUp(request2);

        //then -- 검증
        fail("회원의 이메일이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "ldy_111@naver.com", "1111", "1111", "0000");
        CreateMemberRequest request2 = new CreateMemberRequest("test1", "ldy_122@naver.com", "1111", "1111", "0000");


        //when -- 동작
        memberService.signUp(request1);
        memberService.signUp(request2);

        //then -- 검증
        fail("회원의 닉네임이 중복되어 예외가 발생해야 한다.");
    }


    @Test
    public void login() throws Exception {
        //given -- 조건
        CreateMemberRequest request = new CreateMemberRequest("test1", "email@naver.com", "1111", "1111", "0000");
        memberService.signUp(request);

        //when -- 동작
        LoginRequest loginRequest = new LoginRequest(request.getEmail(), request.getPassword1());
        String token = memberService.login(loginRequest);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void createToken_isMatch() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        boolean result = encryptHelper.isMatch(password, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void createToken_isMatch_Fail() throws Exception {
        //given -- 조건
        String password = "1234";
        String hashedPassword = encryptHelper.encrypt(password);

        //when -- 동작
        String failPassword = "1233";
        boolean result = encryptHelper.isMatch(failPassword, hashedPassword);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }


    @Test(expected = NullMemberException.class)
    public void 회원_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Member member = memberService.findById(100L);

        //then -- 검증
        fail("해당 memberId에 일치하는 회원 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 회원_비밀번호_수정() throws Exception {
        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "email1@naver.com", "1111", "1111", "0000");
        Long signUpId = memberService.signUp(request1);

        //when -- 동작
        Member getMember = memberService.findById(signUpId);
        UpdateMemberPasswordRequest request2 = new UpdateMemberPasswordRequest("1111", "2222");
        memberService.updatePassword(getMember.getId(), request2);

        //then -- 검증
        Member updatedMember = memberService.findById(signUpId);
        Assertions.assertThat(getMember.getPassword()).isEqualTo(updatedMember.getPassword());
    }

    @Test(expected = IllegalValueException.class)
    public void 회원_비밀번호_수정_실패() throws Exception {
        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "email1@naver.com", "1111", "1111", "0000");
        Long signUpId = memberService.signUp(request1);

        //when -- 동작
        Member getMember = memberService.findById(signUpId);
        UpdateMemberPasswordRequest request2 = new UpdateMemberPasswordRequest("1232132131", "2222");
        memberService.updatePassword(getMember.getId(), request2);

        //then -- 검증
        fail("oldPassword가 일치하지 않아 예외가 발생해야 한다.");
    }

    @Test
    public void 회원_프로필사진_수정() throws Exception {
        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "email1@naver.com", "1111", "1111", "0000");
        Long signUpId = memberService.signUp(request1);

        //when -- 동작
        UpdateMemberPhotoPathRequest request2 = new UpdateMemberPhotoPathRequest("새로운 사진 경로");
        memberService.uploadPhotoPath(signUpId, request2);

        //then -- 검증
        Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getMemberPhotoPath()).isEqualTo(request2.getPhotoPath());
    }

    @Test
    public void updateMemberNickname() throws Exception {
        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "email1@naver.com", "1111", "1111", "0000");
        Long signUpId = memberService.signUp(request1);

        //when -- 동작
        UpdateMemberNicknameRequest request = new UpdateMemberNicknameRequest("newNickname");
        memberService.updateMemberNickname(signUpId, request);

        //then -- 검증
        Member member = memberService.findById(signUpId);
        Assertions.assertThat(member.getNickname()).isEqualTo(request.getNickname());
    }

    @Test(expected = NullMemberException.class)
    public void 회원_탈퇴() throws Exception {
        //given -- 조건
        CreateMemberRequest request1 = new CreateMemberRequest("test1", "email1@naver.com", "1111", "1111", "0000");
        Long signUpId = memberService.signUp(request1);

        //when -- 동작
        memberService.deleteMember(signUpId);

        //then -- 검증
        Member member1 = memberService.findById(signUpId);
        fail("탈퇴한 회원 정보에 접근하여 예외가 발생해야 한다.");
    }
}