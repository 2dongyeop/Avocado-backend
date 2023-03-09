package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.BoardReply;
import io.wisoft.capstonedesign.exception.nullcheck.NullBoardReplyException;
import io.wisoft.capstonedesign.repository.BoardReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardReplyService {

    private final BoardReplyRepository boardReplyRepository;

    /**
     * 게시글댓글 저장
     */
    public Long save(BoardReply boardReply) {
        boardReplyRepository.save(boardReply);
        return boardReply.getId();
    }


    /**
     * 게시글댓글 삭제
     */
    public void deleteBoardReply(Long boardReplyId) {
        BoardReply boardReply = boardReplyRepository.findOne(boardReplyId);
        boardReply.delete();
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
}
