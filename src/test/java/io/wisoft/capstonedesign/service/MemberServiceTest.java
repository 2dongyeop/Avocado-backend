package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.repository.MemberRepository;
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
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원_저장() throws Exception {
        //given -- 조건
        Member member = Member.newInstance("test1", "ldy_1204@naver.com", "1111", "0000");

        //when -- 동작
        Long signUpId = memberService.signUp(member);

        //then -- 검증
        Member getMember = memberService.findOne(signUpId);
        Assertions.assertThat(getMember).isEqualTo(member);
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_이메일_중복_검증() throws Exception {

        //given -- 조건
        Member member1 = Member.newInstance("test1", "ldy_1204@naver.com", "1111", "0000");
        Member member2 = Member.newInstance("test2", "ldy_1204@naver.com", "1111", "0000");

        //when -- 동작
        memberService.signUp(member1);
        memberService.signUp(member2);

        //then -- 검증
        fail("회원의 이메일이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원_닉네임_중복_검증() throws Exception {

        //given -- 조건
        Member member1 = Member.newInstance("test1", "ldy_2222@naver.com", "1111", "0000");
        Member member2 = Member.newInstance("test1", "ldy_1111@naver.com", "1111", "0000");

        //when -- 동작
        memberService.signUp(member1);
        memberService.signUp(member2);

        //then -- 검증
        fail("회원의 닉네임이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = NullMemberException.class)
    public void 회원_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Member member = memberService.findOne(2L);

        //then -- 검증
        fail("해당 memberId에 일치하는 회원 정보가 없어 예외가 발생해야 한다.");
    }
}