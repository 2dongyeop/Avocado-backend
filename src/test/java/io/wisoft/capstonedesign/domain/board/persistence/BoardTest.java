package io.wisoft.capstonedesign.domain.board.persistence;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardTest {

    @Test
    public void delete_success() throws Exception {
        //given -- 조건
        final Board board = defaultBoard();

        //when -- 동작
        board.delete();

        //then -- 검증
        Assertions.assertThat(board.getStatus()).isEqualTo(BoardStatus.DELETE);
    }


    @Test
    public void delete_fail() throws Exception {
        //given -- 조건
        final Board board = defaultBoard();

        //when -- 동작
        //then -- 검증
        board.delete();
        assertThrows(IllegalStateException.class, () -> {
            board.delete();
        });
    }


    @Test
    public void update_success() throws Exception {
        //given -- 조건
        final Board board = defaultBoard();

        //when -- 동작
        board.updateTitleBody("newTitle", "newBody");

        //then -- 검증
        Assertions.assertThat(board.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(board.getBody()).isEqualTo("newBody");
    }


    private Board defaultBoard() {

        return Board.builder()
                .member(Member.builder()
                        .nickname("memberNickname")
                        .email("memberEmail")
                        .password("memberPassword")
                        .phoneNumber("memberPhonenumber")
                        .build())
                .title("title")
                .body("body")
                .dept(HospitalDept.DENTAL)
                .build();
    }
}