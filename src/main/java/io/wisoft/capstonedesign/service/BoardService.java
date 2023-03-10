package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long save(Long memberId, String title, String body) {

        //엔티티 조회
        Member member = memberService.findOne(memberId);

        Board board = Board.createBoard(member, title, body);

        boardRepository.save(board);
        return board.getId();
    }

    @Transactional
    public Long save(Long memberId, String title, String body, String boardPhotoPath) {

        //엔티티 조회
        Member member = memberService.findOne(memberId);

        Board board = Board.createBoard(member, title, body, boardPhotoPath);

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
    public List<Board> findByMemberId(Long memberId) { return boardRepository.findByMemberId(memberId); }

    public Board findOne(Long boardId) {
        Board getBoard = boardRepository.findOne(boardId);

        if (getBoard == null) {
            throw new NullBoardException("해당 게시글은 존재하지 않습니다.");
        }
        return getBoard;
    }

    public List<Board> findAll() { return boardRepository.findAll(); }
}
