package io.wisoft.capstonedesign.domain.boardreply.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardReplyRepositoryTest {

    @Autowired BoardReplyRepository boardReplyRepository;

    @Test
    public void findByBoardIdOrderByCreateAsc() throws Exception {
        //given -- 조건

        //when -- 동작
        final List<BoardReply> list = boardReplyRepository.findByBoardIdOrderByCreateAsc();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(5);
    }
}