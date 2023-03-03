package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long save(Board board) {
        boardRepository.save(board);
        return board.getId();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.delete();
    }

    /* 조회 로직 */
    public List<Board> findByMember(Long memberId) { return boardRepository.findByMember(memberId); }

    public Board findOne(Long boardId) { return boardRepository.findOne(boardId); }

    public List<Board> findAll() { return boardRepository.findAll(); }
}
