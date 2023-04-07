package io.wisoft.capstonedesign.domain.pick.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PickRepositoryTest {

    @Autowired PickRepository pickRepository;

    @Test
    public void findByMemberId() throws Exception {
        //given -- 조건

        //when -- 동작
        List<Pick> list = pickRepository.findByMemberIdOrderByCreateAtAsc(1L);

        //then -- 검증
        assertThat(list.size()).isEqualTo(1);
    }
}