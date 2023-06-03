package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.persistence.BoardRepository;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardException;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        final Member member = memberService.findById(request.memberId());

        final Board board = createBoard(request, member);

        boardRepository.save(board);
        return board.getId();
    }

    private Board createBoard(final CreateBoardRequest request, final Member member) {
        return Board.builder()
                .member(member)
                .title(request.title())
                .body(request.body())
                .dept(HospitalDept.valueOf(request.dept()))
                .build();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(final Long boardId) {
        final Board board = findById(boardId);
        board.delete();
    }

    /**
     * 게시글 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long boardId, final UpdateBoardRequest request) {

        final Board board = findById(boardId);
        board.updateTitleBody(request.newTitle(), request.newBody());
    }

    /* 조회 로직 */
    /** 게시글 단건 상세 조회 */
    public Board findDetailById(final Long boardId) {
        return boardRepository.findDetailById(boardId).orElseThrow(NullBoardException::new);
    }

    /** 게시글 단건 조회 */
    public Board findById(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NullBoardException::new);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }


    /** 게시글 목록을 페이징 조회 */
    public Page<Board> findAllUsingPaging(final Pageable pageable) {
        return boardRepository.findAllUsingPaging(pageable);
    }

    public List<Board> findAllByMember() {
        return boardRepository.findAllByMember();
    }
}