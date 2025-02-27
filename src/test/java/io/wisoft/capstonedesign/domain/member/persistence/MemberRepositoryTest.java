package io.wisoft.capstonedesign.domain.member.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void findByEmail() throws Exception {
        //given -- 조건
        final String email = "ldy_1204@naver.com";

        //when -- 동작
        final Member member = memberRepository.findByEmail(email).get();

        //then -- 검증
        assertThat(member.getNickname()).isEqualTo("이동엽");
    }
}