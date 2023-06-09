package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static io.wisoft.capstonedesign.setting.data.MemberTestData.getDefaultMember;
import static org.junit.jupiter.api.Assertions.*;

public class BoardServiceTest extends ServiceTest {

    @Autowired EntityManager em;
    @Autowired BoardService boardService;

    @Test
    public void save_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request = getCreateBoardRequest(member.getId());

        //when -- 동작
        final Long saveId = boardService.save(request);

        //then -- 검증
        final Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.WRITE);
    }

    @Test
    public void save_fail_notFoundMember() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request = getCreateBoardRequest(10102021L);

        //when -- 동작
        //then -- 검증
        assertThrows(NullMemberException.class, () -> {
            boardService.save(request);
        });
    }

    @Test
    public void deleteBoard_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = getCreateBoardRequest(member.getId());

        final Long saveId = boardService.save(request);

        //when -- 동작
        boardService.deleteBoard(saveId);

        //then -- 검증
        final Board getBoard = boardService.findById(saveId);

        Assertions.assertThat(getBoard.getStatus()).isEqualTo(BoardStatus.DELETE);
    }


    @Test
    public void deleteBoard_fail_duplicate_request() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = getCreateBoardRequest(member.getId());
        final Long saveId = boardService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalStateException.class, () -> {
            boardService.deleteBoard(saveId);
            boardService.deleteBoard(saveId);
        });
    }


    @Test
    public void findById() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = getCreateBoardRequest(member.getId());
        final Long saveId = boardService.save(request);

        //when -- 동작
        final Board getBoard = boardService.findById(saveId);

        //then -- 검증
        Assertions.assertThat(getBoard.getTitle()).isEqualTo(request.title());
    }

    @Test
    public void findById_fail() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request = getCreateBoardRequest(member.getId());
        boardService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullBoardException.class, () -> {
            boardService.findById(11293218L);
        });
    }

    @Test
    public void 게시글_수정() throws Exception {

        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request1 = getCreateBoardRequest(member.getId());
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

    @Test
    public void 게시글_수정_실패() throws Exception {

        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성 및 저장
        final CreateBoardRequest request1 = getCreateBoardRequest(member.getId());
        final Long saveId = boardService.save(request1);

        final UpdateBoardRequest request2 = new UpdateBoardRequest(null, "본문2");

        //when -- 동작
        //then - 검증
        assertThrows(IllegalValueException.class, () -> {
            final Board board = boardService.findById(saveId);
            boardService.updateTitleBody(board.getId(), request2);
        });
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

    private CreateBoardRequest getCreateBoardRequest(final Long memberId) {
        return CreateBoardRequest.builder().memberId(memberId).title("title").body("body").dept("OBSTETRICS").boardPhotoPath("path").build();
    }
}