package io.wisoft.capstonedesign.domain.board.persistence;

import org.assertj.core.api.Assertions;
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
        final PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final Page<Board> page = boardRepository.findAllUsingPaging(pageRequest);
        final List<Board> content = page.getContent();
//        Board board1 = content.get(0);
//        Board board2 = content.get(1);

        //then -- 검증
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
//        assertThat(board1.getCreateAt()).isBefore(board2.getCreateAt());
    }


    @Test
    public void findAllByMember() throws Exception {
        //given -- 조건

        //when -- 동작
        final List<Board> boardList = boardRepository.findAllByMember();

        //then -- 검증
        Assertions.assertThat(boardList.size()).isEqualTo(3);
    }
}