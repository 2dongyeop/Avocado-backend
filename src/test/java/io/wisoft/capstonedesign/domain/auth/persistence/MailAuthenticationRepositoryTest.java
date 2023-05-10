package io.wisoft.capstonedesign.domain.auth.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MailAuthenticationRepositoryTest {

    @Autowired
    MailAuthenticationRepository mailAuthenticationRepository;

    @Test
    public void findByEmail() throws Exception {

        final String email = "test_email";
        //given -- 조건

        final DBMailAuthentication saved = mailAuthenticationRepository.save(DBMailAuthentication.builder()
                .email(email)
                .isVerified(true)
                .build());

        //when -- 동작
        final DBMailAuthentication getMail = mailAuthenticationRepository.findByEmail(email).orElseThrow();

        //then -- 검증
        Assertions.assertThat(saved).isEqualTo(getMail);
        Assertions.assertThat(saved.getEmail()).isEqualTo(getMail.getEmail());
        Assertions.assertThat(saved.isVerified()).isTrue();
    }

    @Test
    public void findByEmail_fail() throws Exception {

        final String email = "test_email";
        //given -- 조건

        final DBMailAuthentication saved = mailAuthenticationRepository.save(DBMailAuthentication.builder()
                .email(email)
                .isVerified(true)
                .build());

        //when -- 동작
        assertThrows(NoSuchElementException.class, () -> {
            mailAuthenticationRepository.findByEmail("not_test_email").orElseThrow();
        });
    }
}