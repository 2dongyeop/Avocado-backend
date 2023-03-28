package io.wisoft.capstonedesign.domain.boardreply.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReplyRepository;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.UpdateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardReplyException;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
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
        Board board = boardService.findOne(request.getBoardId());
        Staff staff = staffService.findOne(request.getStaffId());

        BoardReply boardReply = BoardReply.createBoardReply(board, staff, request.getReply());

        boardReplyRepository.save(boardReply);
        return boardReply.getId();
    }


    /**
     * 게시글댓글 삭제
     */
    @Transactional
    public void deleteBoardReply(final Long boardReplyId) {
        BoardReply boardReply = findOne(boardReplyId);
        boardReply.delete();
    }

    /**
     * 게시글 댓글 수정
     */
    @Transactional
    public void update(final Long boardReplyId, final UpdateBoardReplyRequest request) {

        validateParameter(request);
        BoardReply boardReply = findOne(boardReplyId);

        boardReply.update(request.getReply());
    }

    private void validateParameter(final UpdateBoardReplyRequest request) {

        if (!StringUtils.hasText(request.getReply())) {
            throw new IllegalValueException();
        }
    }

    /**
     * 게시글댓글 단건 조회
     */
    public BoardReply findOne(final Long boardReplyId) {
        return boardReplyRepository.findOne(boardReplyId).orElseThrow(NullBoardReplyException::new);
    }

    /**
     * 특정 게시글의 게시글댓글 목록 조회
     */
    public List<BoardReply> findByBoardId(final Long boardId) { return boardReplyRepository.findByBoardId(boardId); }

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
