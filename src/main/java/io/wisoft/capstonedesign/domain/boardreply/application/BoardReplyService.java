package io.wisoft.capstonedesign.domain.boardreply.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReplyRepository;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.UpdateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardReplyService {

    private final BoardReplyRepository boardReplyRepository;
    private final BoardService boardService;
    private final StaffService staffService;

    /**
     * 게시글댓글 저장
     */
    @Transactional
    public Long save(final CreateBoardReplyRequest request) {

        //엔티티 조회
        final Board board = boardService.findById(request.boardId());
        final Staff staff = staffService.findById(request.staffId());

        final BoardReply boardReply = createBoardReply(request, board, staff);

        boardReplyRepository.save(boardReply);
        return boardReply.getId();
    }

    private BoardReply createBoardReply(final CreateBoardReplyRequest request, final Board board, final Staff staff) {
        return BoardReply.builder()
                .board(board)
                .staff(staff)
                .reply(request.reply())
                .build();
    }


    /**
     * 게시글댓글 삭제
     */
    @Transactional
    public void deleteBoardReply(final Long boardReplyId) {
        boardReplyRepository.delete(findById(boardReplyId));
    }

    /**
     * 게시글 댓글 수정
     */
    @Transactional
    public void update(final Long boardReplyId, final UpdateBoardReplyRequest request) {

        validateParameter(request);
        BoardReply boardReply = findById(boardReplyId);

        boardReply.update(request.reply());
    }

    private void validateParameter(final UpdateBoardReplyRequest request) {

        if (!StringUtils.hasText(request.reply())) {
            throw new IllegalValueException("수정할 내용이 비어있습니다.", ErrorCode.ILLEGAL_PARAM);
        }
    }

    /**
     * 게시글댓글 단건 조회
     */
    public BoardReply findById(final Long boardReplyId) {
        return boardReplyRepository.findById(boardReplyId).orElseThrow(NotFoundException::new);
    }

    /**
     * 게시글댓글 단건 상세 조회
     */
    public BoardReply findDetailById(final Long boardReplyId) {
        return boardReplyRepository.findDetailById(boardReplyId).orElseThrow(NotFoundException::new);
    }

    /**
     * 특정게시글의 댓글 목록 오름차순 조회
     */
    public List<BoardReply> findByBoardIdOrderByCreateAsc() {
        return boardReplyRepository.findByBoardIdOrderByCreateAsc();
    }

    /**
     * 특정게시글의 댓글 목록 내림차순 조회
     */
    public List<BoardReply> findByBoardIdOrderByCreateDesc() {
        return boardReplyRepository.findByBoardIdOrderByCreateDesc();
    }
}
