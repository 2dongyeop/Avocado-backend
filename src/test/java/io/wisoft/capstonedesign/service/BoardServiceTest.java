package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.board.Board;
import io.wisoft.capstonedesign.board.BoardService;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.board.BoardRepository;
import jakarta.persistence.EntityManager;
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

    @Autowired EntityManager em;
    @Autowired
    BoardService boardService;
    @Autowired BoardRepository boardRepository;

    //게시글 작성
    @Test
    public void 게시글작성() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("test", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //when -- 동작
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Long saveId = boardService.save(member.getId(), "test code!", "I write test code!", dept);

        //then -- 검증
        Board getBoard = boardRepository.findOne(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.WRITE);
    }

    //게시글 삭제
    @Test
    public void 게시글삭제() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("test", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //게시글 생성 및 저장
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Long saveId = boardService.save(member.getId(), "test code!", "I write test code!", dept);

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
        em.persist(member);

        //게시글 생성 및 저장
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Long saveId = boardService.save(member.getId(), "test code!", "I write test code!", dept);

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
        Board board = boardService.findOne(100L);

        //then -- 검증
        fail("해당 boardId에 일치하는 게시글 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 게시글_수정() throws Exception {

        //given -- 조건
        Member member = Member.newInstance("test","ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        Board board = Board.createBoard(member, "제목1", "본문1", HospitalDept.OBSTETRICS);
        em.persist(board);

        //when -- 동작
        boardService.updateTitleBody(board.getId(), "제목2", "본문2");

        //then -- 검증
        Assertions.assertThat(board.getTitle()).isEqualTo("제목2");
        Assertions.assertThat(board.getBody()).isEqualTo("본문2");
        Assertions.assertThat(board.getUpdateAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 게시글_수정_실패() throws Exception {

        //given -- 조건
        Member member = Member.newInstance("test","ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        Board board = Board.createBoard(member, "제목1", "본문1", HospitalDept.OBSTETRICS);
        em.persist(board);

        //when -- 동작
        boardService.updateTitleBody(board.getId(), null, "본문2");

        //then - 검증
        fail("제목이 비어있어 예외가 발생해야 한다.");
    }
}