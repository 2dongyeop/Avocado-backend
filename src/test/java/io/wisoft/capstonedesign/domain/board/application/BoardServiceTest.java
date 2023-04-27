package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Test
    public void save_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();

        //when -- 동작
        final Long saveId = boardService.save(request);

        //then -- 검증
        final Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.WRITE);
    }

    @Test(expected = NullMemberException.class)
    public void save_fail_notFoundMember() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(10102021L)  //Not Found Member
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();

        //when -- 동작
        boardService.save(request);

        //then -- 검증
        fail("해당 id의 회원이 존재하지 않아 예외가 발생해야 한다.");
    }

    @Test
    public void deleteBoard_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();

        final Long saveId = boardService.save(request);

        //when -- 동작
        boardService.deleteBoard(saveId);

        //then -- 검증
        final Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.DELETE);
    }


    @Test(expected = IllegalStateException.class)
    public void deleteBoard_fail_duplicate_request() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();
        final Long saveId = boardService.save(request);

        //when -- 동작
        boardService.deleteBoard(saveId);
        boardService.deleteBoard(saveId);

        //then -- 검증
        fail("중복 삭제 요청으로 인한 예외가 발생해야 한다.");
    }


    @Test
    public void findById() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();
        final Long saveId = boardService.save(request);

        //when -- 동작
        final Board getBoard = boardService.findById(saveId);

        //then -- 검증
        Assertions.assertThat(getBoard.getTitle()).isEqualTo(request.title());
    }

    @Test(expected = NullBoardException.class)
    public void findById_fail() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();
        boardService.save(request);

        //when -- 동작
        boardService.findById(11293218L);

        //then -- 검증
        fail("해당 boardId에 일치하는 게시글 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 게시글_수정() throws Exception {

        //given -- 조건
        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request1 = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();
        final Long saveId = boardService.save(request1);

        final UpdateBoardRequest request2 = new UpdateBoardRequest("newTitle", "newBody");

        //when -- 동작
        final Board board = boardService.findById(saveId);
        boardService.updateTitleBody(board.getId(), request2);

        //then -- 검증
//        Assertions.assertThat(updatedBoard.getUpdatedAt().isAfter(board.getUpdatedAt())).isTrue();
        Assertions.assertThat(board.getTitle()).isEqualTo(request2.newTitle());
        Assertions.assertThat(board.getBody()).isEqualTo(request2.newBody());
        Assertions.assertThat(board.getUpdatedAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 게시글_수정_실패() throws Exception {

        //given -- 조건
        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request1 = CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("OBSTETRICS")
                .boardPhotoPath("path")
                .build();
        final Long saveId = boardService.save(request1);

        final UpdateBoardRequest request2 = new UpdateBoardRequest(null, "본문2");

        //when -- 동작
        final Board board = boardService.findById(saveId);
        boardService.updateTitleBody(board.getId(), request2);

        //then - 검증
        fail("제목이 비어있어 예외가 발생해야 한다.");
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건
        final PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<Board> page = boardService.findAllUsingPaging(pageRequest).getContent();

        //then -- 검증
        Assertions.assertThat(page.size()).isEqualTo(3);
    }
}