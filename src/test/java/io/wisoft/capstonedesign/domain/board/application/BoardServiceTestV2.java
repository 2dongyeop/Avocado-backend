package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.persistence.BoardRepository;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTestV2 {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberService memberService;

    @Nested
    @DisplayName("게시글 작성")
    class CreateBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 작성에 성공한다.")
        public void 성공() throws Exception {

            //given
            final Member member = Member.newInstance(
                    "게시글작성성공",
                    "게시글작성성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final var request = new CreateBoardRequest(
                    member.getId(),
                    "title",
                    "body",
                    "DENTAL",
                    "photo"
            );

            given(memberService.findById(any())).willReturn(member);

            //when
            boardService.save(request);

            //then
            Mockito.verify(boardRepository, Mockito.times(1)).save(any());
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeleteBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 삭제에 성공한다.")
        public void 성공() throws Exception {

            //given
            final Long boardId = 1L;

            final Member member = Member.newInstance(
                    "게시글삭제성공",
                    "게시글삭제성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

            //when
            boardService.deleteBoard(1L);

            //then
            Assertions.assertThat(board.getStatus()).isEqualTo(BoardStatus.DELETE);
        }


        @Test
        @DisplayName("이미 삭제된 게시글에 중복 요청을 하여, 요청이 실패한다.")
        public void 실패1() throws Exception {

            //given
            final Long boardId = 1L;

            final Member member = Member.newInstance(
                    "게시글삭제성공",
                    "게시글삭제성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

            //expected
            assertThrows(IllegalStateException.class, () -> {
                boardService.deleteBoard(1L);
                boardService.deleteBoard(1L);
            });
        }
    }


    @Nested
    @DisplayName("게시글 수정")
    class UpdateBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 수정에 성공한다.")
        public void 성공1() throws Exception {

            //given
            final Long boardId = 1L;

            final Member member = Member.newInstance(
                    "게시글수정성공1",
                    "게시글수정성공1@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

            final var request = new UpdateBoardRequest("newTitle", "newBody");

            //when
            boardService.updateTitleBody(boardId, request);

            //then
            Assertions.assertThat(board.getTitle()).isEqualTo("newTitle");
            Assertions.assertThat(board.getBody()).isEqualTo("newBody");
        }
    }


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("게시글 단건 조회 성공")
        public void 성공1() throws Exception {

            //given
            final Long boardId = 1L;

            final Member member = Member.newInstance(
                    "게시글수정성공1",
                    "게시글수정성공1@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

            //when
            final Board findBoard = boardService.findById(boardId);

            //then
            Assertions.assertThat(findBoard.getMember()).isEqualTo(member);
            Assertions.assertThat(findBoard.getTitle()).isEqualTo("title");
            Assertions.assertThat(findBoard.getBody()).isEqualTo("body");
        }


        @Test
        @DisplayName("게시글 목록 조회 성공")
        public void 성공2() throws Exception {

            //given
            final Member member = Member.newInstance(
                    "게시글수정성공1",
                    "게시글수정성공1@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board1 = Board.createBoard(
                    member,
                    "title1",
                    "body1",
                    HospitalDept.DENTAL
            );

            final Board board2 = Board.createBoard(
                    member,
                    "title2",
                    "body2",
                    HospitalDept.DENTAL
            );

            given(boardRepository.findAll()).willReturn(List.of(board1, board2));

            //when
            final List<Board> boardList = boardService.findAll();

            //then
            Assertions.assertThat(boardList.size()).isEqualTo(2);
            Assertions.assertThat(boardList.get(0).getTitle()).isEqualTo("title1");
            Assertions.assertThat(boardList.get(0).getBody()).isEqualTo("body1");
        }
    }
}
