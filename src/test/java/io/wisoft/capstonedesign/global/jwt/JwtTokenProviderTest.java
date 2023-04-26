package io.wisoft.capstonedesign.global.jwt;

import io.wisoft.capstonedesign.global.exception.token.IllegalTokenException;
import io.wisoft.capstonedesign.global.exception.token.NotExistTokenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JwtTokenProviderTest {

    @Autowired JwtTokenProvider jwtTokenProvider;

    @Test
    public void createToken_success() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";

        //when -- 동작
        String token = jwtTokenProvider.createToken(subject);

        //then -- 검증
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void getSubject_success() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";
        String token = jwtTokenProvider.createToken(subject);

        //when -- 동작
        String getSubject = jwtTokenProvider.getSubject(token);

        //then -- 검증
        Assertions.assertThat(subject).isEqualTo(getSubject);
    }

    @Test
    public void validateToken_success() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";
        String token = jwtTokenProvider.createToken(subject);

        //when -- 동작
        boolean result = jwtTokenProvider.validateToken(token);

        //then -- 검증
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void validateToken_fail_isBlackList() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";
        String token = jwtTokenProvider.createToken(subject);

        jwtTokenProvider.addBlackList(token);

        //when -- 동작
        boolean result = jwtTokenProvider.validateToken(token);

        //then -- 검증
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void addBlackList_success() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";
        String token = jwtTokenProvider.createToken(subject);

        //when -- 동작
        int blackListCount = jwtTokenProvider.addBlackList(token);

        //then -- 검증
        Assertions.assertThat(blackListCount).isEqualTo(1);
    }


    @Test
    public void addBlackList_fail_isContains() throws Exception {
        //given -- 조건
        String subject = "test-token-subject";
        String token = jwtTokenProvider.createToken(subject);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalTokenException.class, () -> {
            jwtTokenProvider.addBlackList(token);
            jwtTokenProvider.addBlackList(token);
        });
    }

    @Test
    public void addBlackList_fail_isNotValidate() throws Exception {
        //given -- 조건
        String token = "not validate token";

        //when -- 동작
        //then -- 검증
        assertThrows(NotExistTokenException.class, () -> {
            jwtTokenProvider.addBlackList(token);
        });
    }
}