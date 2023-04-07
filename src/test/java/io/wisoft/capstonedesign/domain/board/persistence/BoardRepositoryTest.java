package io.wisoft.capstonedesign.domain.board.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    @Test
    public void paging() throws Exception {
        //given -- 조건
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));

        //when -- 동작
        Page<Board> page = boardRepository.findAllUsingPagingOrderByCreateAtAsc(pageRequest);
        List<Board> content = page.getContent();
        Board board1 = content.get(0);
        Board board2 = content.get(1);

        //then -- 검증
        assertThat(content.size()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
//        assertThat(board1.getCreateAt()).isBefore(board2.getCreateAt());
    }
}