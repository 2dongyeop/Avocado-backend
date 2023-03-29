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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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

        Board board = Board.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .dept(HospitalDept.valueOf(request.getDept()))
                .build();

        boardRepository.save(board);
        return board.getId();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(final Long boardId) {
        Board board = findOne(boardId);
        board.delete();
    }

    /**
     * 게시글 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long boardId, final UpdateBoardRequest request) {

        validateTitleBody(request);
        Board board = findOne(boardId);

        board.updateTitleBody(request.getNewTitle(), request.getNewBody());
    }

    private void validateTitleBody(final UpdateBoardRequest request) {

        if (!StringUtils.hasText(request.getNewBody()) || !StringUtils.hasText(request.getNewTitle())) {
            throw new IllegalValueException("파라미터가 비어있어 업데이트할 수 없습니다.");
        }
    }

    /* 조회 로직 */
    public List<Board> findByMemberId(final Long memberId) {
        return boardRepository.findByMemberId(memberId);
    }

    public Board findOne(final Long boardId) {
        return boardRepository.findOne(boardId).orElseThrow(NullBoardException::new);
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