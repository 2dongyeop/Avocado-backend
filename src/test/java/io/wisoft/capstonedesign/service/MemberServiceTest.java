package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.DuplicateMemberException;
import io.wisoft.capstonedesign.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
    public void 회원가입() throws Exception {
        //given -- 조건
        Member member = Member.newInstance("ldy_1204@naver.com", "1111", "0000");

        //when -- 동작
        Long signUpId = memberService.signUp(member);

        //then -- 검증
        //assertEquals(member, memberRepository.findOne(signUpId));
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(signUpId));
    }

    @Test(expected = DuplicateMemberException.class)
    public void 회원중복검증() throws Exception {
        //given -- 조건
        Member member1 = Member.newInstance("ldy_1204@naver.com", "1111", "0000");
        Member member2 = Member.newInstance("ldy_1204@naver.com", "2222", "0000");

        //when -- 동작
        memberService.signUp(member1);
        memberService.signUp(member2);

        //then -- 검증
        fail("회원의 이메일이 중복되어 예외가 발생해야 한다.");
    }
}