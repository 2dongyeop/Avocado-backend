package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.persistence.BoardRepository;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
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
    public Long save(final CreateBoardRequest request) {

        //엔티티 조회
        Member member = memberService.findOne(request.getMemberId());
        Board board = Board.createBoard(member, request.getTitle(), request.getBody(), HospitalDept.valueOf(request.getDept()));

        boardRepository.save(board);
        return board.getId();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(final Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.delete();
    }

    /**
     * 게시글 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long boardId, final UpdateBoardRequest request) {

        Board board = findOne(boardId);
        validateTitleBody(request.getNewTitle(), request.getNewBody());

        board.updateTitleBody(request.getNewTitle(), request.getNewBody());
    }

    private void validateTitleBody(final String newTitle, final String newBody) {
        if (newTitle == null || newBody == null) {
            throw new IllegalValueException("게시글의 제목이나 본문이 비어있습니다.");
        }
    }

    /* 조회 로직 */
    public List<Board> findByMemberId(final Long memberId) {
        return boardRepository.findByMemberId(memberId);
    }

    public Board findOne(final Long boardId) {
        Board getBoard = boardRepository.findOne(boardId);

        if (getBoard == null) {
            throw new NullBoardException("해당 게시글은 존재하지 않습니다.");
        }
        return getBoard;
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public List<Board> findAllOrderByCreateAtAsc() {
        return boardRepository.findAllOrderByCreateAtAsc();
    }

    public List<Board> findAllOrderByCreateAtDesc() {
        return boardRepository.findAllOrderByCreateAtDesc();
    }

    public List<Board> findAllByMember() {
        return boardRepository.findAllByMember();
    }

    public List<Board> findByStaffReply(final Long staffId) {
        return boardRepository.findByStaffReply(staffId);
    }
}