package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.BoardReply;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.exception.IllegalValueException;
import io.wisoft.capstonedesign.exception.nullcheck.NullBoardReplyException;
import io.wisoft.capstonedesign.repository.BoardReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long save(Long boardId, Long staffId, String reply) {

        //엔티티 조회
        Board board = boardService.findOne(boardId);
        Staff staff = staffService.findOne(staffId);

        BoardReply boardReply = BoardReply.createBoardReply(board, staff, reply);

        boardReplyRepository.save(boardReply);
        return boardReply.getId();
    }


    /**
     * 게시글댓글 삭제
     */
    @Transactional
    public void deleteBoardReply(Long boardReplyId) {
        BoardReply boardReply = boardReplyRepository.findOne(boardReplyId);
        boardReply.delete();
    }

    /**
     * 게시글 댓글 수정
     */
    @Transactional
    public void update(Long boardReplyId, String reply) {

        BoardReply boardReply = findOne(boardReplyId);

        validateParameter(reply);
        boardReply.update(reply);
    }

    private void validateParameter(String reply) {

        if (reply == null) {
            throw new IllegalValueException("댓글이 비어있습니다.");
        }
    }

    /**
     * 게시글댓글 단건 조회
     */
    public BoardReply findOne(Long boardReplyId) {
        BoardReply getBoardReply = boardReplyRepository.findOne(boardReplyId);
        if (getBoardReply == null) {
            throw new NullBoardReplyException("해당 게시글댓글은 존재하지 않습니다.");
        }
        return getBoardReply;
    }

    /**
     * 특정 게시글의 게시글댓글 목록 조회
     */
    public List<BoardReply> findByBoardId(Long boardId) { return boardReplyRepository.findByBoardId(boardId); }

    /**
     * 특정게시글의 댓글 목록 오름차순 조회
     */
    public List<BoardReply> findByBoardIdcreateAtASC() {
        return boardReplyRepository.findByBoardIdcreateAtASC();
    }

    /**
     * 특정게시글의 댓글 목록 내림차순 조회
     */
    public List<BoardReply> findByBoardIdcreateAtDESC() {
        return boardReplyRepository.findByBoardIdcreateAtDESC();
    }
}
