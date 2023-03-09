package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.enumeration.BoardStatus;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.repository.BoardRepository;
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
public class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;

    //게시글 작성
    @Test
    public void 게시글작성() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("test", "ldy_1204@naver.com", "1111", "0000");
        //게시글 생성
        Board board = Board.createBoard(member, "test code!", "I write test code!");

        //when -- 동작
        Long saveId = boardService.save(board);

        //then -- 검증
        Board getBoard = boardRepository.findOne(saveId);

        Assertions.assertThat(board).isEqualTo(getBoard);
        Assertions.assertThat(board.getStatus()).isEqualTo(BoardStatus.WRITE);
    }

    //게시글 삭제
    @Test
    public void 게시글삭제() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("test", "ldy_1204@naver.com", "1111", "0000");
        //게시글 생성
        Board board = Board.createBoard(member, "test code!", "I write test code!");
        //게시글 저장
        Long saveId = boardService.save(board);

        //when -- 동작
        boardService.deleteBoard(saveId);

        //then -- 검증
        Board getBoard = boardService.findOne(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.DELETE);
    }

    @Test(expected = IllegalStateException.class)
    public void 게시물_중복_삭제요청() throws Exception {
        //given -- 조건
        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("test","ldy_1204@naver.com", "1111", "0000");
        //게시글 생성
        Board board = Board.createBoard(member, "test code!", "I write test code!");
        //게시글 저장
        Long saveId = boardService.save(board);


        //when -- 동작
        boardService.deleteBoard(saveId);
        boardService.deleteBoard(saveId);

        //then -- 검증
        fail("중복 삭제 요청으로 인한 예외가 발생해야 한다.");
    }

    @Test(expected = NullBoardException.class)
    public void 게시글_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Board board = boardService.findOne(2L);

        //then -- 검증
        fail("해당 boardId에 일치하는 게시글 정보가 없어 예외가 발생해야 한다.");
    }
}