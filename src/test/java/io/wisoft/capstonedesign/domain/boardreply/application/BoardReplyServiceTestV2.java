package io.wisoft.capstonedesign.domain.boardreply.application;

import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReplyRepository;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.UpdateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardReplyServiceTestV2 {

    @InjectMocks
    private BoardReplyService boardReplyService;

    @Mock
    private BoardReplyRepository boardReplyRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private StaffService staffService;

    @Nested
    @DisplayName("게시글 댓글 저장")
    class CreateBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 댓글 작성에 실패한다.")
        void 성공() throws Exception {

            //given
            final Member member = Member.newInstance(
                    "게시글댓글저장성공",
                    "게시글댓글저장성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            final var request = new CreateBoardReplyRequest(
                    1L,
                    1L,
                    "reply"
            );

            final Staff staff = Staff.newInstance(
                    Hospital.createHospital(
                            "게시글댓글저장성공병원",
                            "number",
                            "address",
                            "oper"
                    ),
                    "name",
                    "게시글댓글저장성공의료진@email.com",
                    "pass12",
                    "license",
                    HospitalDept.DENTAL
            );

            final BoardReply boardReply = BoardReply.createBoardReply(
                    board,
                    staff,
                    "reply"
            );

            given(boardService.findById(any())).willReturn(board);
            given(staffService.findById(any())).willReturn(staff);
            given(boardReplyRepository.save(any())).willReturn(boardReply);

            //when
            boardReplyService.save(request);

            //then
            verify(boardReplyRepository, times(1)).save(any());
        }
    }


    @Nested
    @DisplayName("게시글 댓글 삭제")
    class DeleteBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 댓글이 삭제되어야 한다.")
        void 성공() throws Exception {

            //given
            final Long boardReplyId = 1L;

            final Member member = Member.newInstance(
                    "게시글댓글저장성공",
                    "게시글댓글저장성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            final Staff staff = Staff.newInstance(
                    Hospital.createHospital(
                            "게시글댓글저장성공병원",
                            "number",
                            "address",
                            "oper"
                    ),
                    "name",
                    "게시글댓글저장성공의료진@email.com",
                    "pass12",
                    "license",
                    HospitalDept.DENTAL
            );

            final BoardReply boardReply = BoardReply.createBoardReply(
                    board,
                    staff,
                    "reply"
            );

            given(boardReplyRepository.findById(any())).willReturn(Optional.of(boardReply));

            //when
            boardReplyService.deleteBoardReply(boardReplyId);

            //then
            verify(boardReplyRepository, times(1)).delete(any());
        }
    }


    @Nested
    @DisplayName("게시글 댓글 수정")
    class UpdateBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 댓글 수정에 성공한다.")
        void 성공() throws Exception {

            //given
            final var boardReplyId = 1L;
            final var request = new UpdateBoardReplyRequest("update-reply");

            final Member member = Member.newInstance(
                    "게시글댓글저장성공",
                    "게시글댓글저장성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Board board = Board.createBoard(
                    member,
                    "title",
                    "body",
                    HospitalDept.DENTAL
            );

            final Staff staff = Staff.newInstance(
                    Hospital.createHospital(
                            "게시글댓글저장성공병원",
                            "number",
                            "address",
                            "oper"
                    ),
                    "name",
                    "게시글댓글저장성공의료진@email.com",
                    "pass12",
                    "license",
                    HospitalDept.DENTAL
            );

            final BoardReply boardReply = BoardReply.createBoardReply(
                    board,
                    staff,
                    "reply"
            );

            given(boardReplyRepository.findById(any())).willReturn(Optional.of(boardReply));

            //when
            boardReplyService.update(boardReplyId, request);

            //then
            Assertions.assertThat(boardReply.getReply()).isEqualTo("update-reply");
        }
    }
}
