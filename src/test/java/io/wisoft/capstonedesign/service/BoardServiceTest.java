package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired EntityManager em;
    @Autowired BoardService boardService;

    //게시글 작성
    @Test
    public void 게시글작성() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        CreateBoardRequest request = new CreateBoardRequest(member.getId(), "test code!", "I write test code!", "OBSTETRICS", "path");

        //when -- 동작
        Long saveId = boardService.save(request);

        //then -- 검증
        Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.WRITE);
    }

    //게시글 삭제
    @Test
    public void 게시글삭제() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장

        CreateBoardRequest request = new CreateBoardRequest(member.getId(), "test code!", "I write test code!", "OBSTETRICS", "path");
        Long saveId = boardService.save(request);

        //when -- 동작
        boardService.deleteBoard(saveId);

        //then -- 검증
        Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.DELETE);
    }

    @Test(expected = IllegalStateException.class)
    public void 게시물_중복_삭제요청() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        CreateBoardRequest request = new CreateBoardRequest(member.getId(), "test code!", "I write test code!", "OBSTETRICS", "path");
        Long saveId = boardService.save(request);

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
        Board board = boardService.findById(100L);

        //then -- 검증
        fail("해당 boardId에 일치하는 게시글 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 게시글_수정() throws Exception {

        //given -- 조건
        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        CreateBoardRequest request1 = new CreateBoardRequest(member.getId(), "test code!", "I write test code!", "OBSTETRICS", "path");
        Long saveId = boardService.save(request1);

        UpdateBoardRequest request2 = new UpdateBoardRequest("제목2", "본문2");

        //when -- 동작
        Board board = boardService.findById(saveId);
        boardService.updateTitleBody(board.getId(), request2);

        //then -- 검증
        Assertions.assertThat(board.getTitle()).isEqualTo(request2.getNewTitle());
        Assertions.assertThat(board.getBody()).isEqualTo(request2.getNewBody());
        Assertions.assertThat(board.getUpdateAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 게시글_수정_실패() throws Exception {

        //given -- 조건
        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        CreateBoardRequest request1 = new CreateBoardRequest(member.getId(), "test code!", "I write test code!", "OBSTETRICS", "path");
        Long saveId = boardService.save(request1);

        UpdateBoardRequest request2 = new UpdateBoardRequest(null, "본문2");

        //when -- 동작
        Board board = boardService.findById(saveId);
        boardService.updateTitleBody(board.getId(), request2);

        //then - 검증
        fail("제목이 비어있어 예외가 발생해야 한다.");
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건


        //when -- 동작
        List<Board> page = boardService.findAllUsingPagingOrderByCreateAtAsc(0);

        //then -- 검증
        Assertions.assertThat(page.size()).isEqualTo(2);
    }
}