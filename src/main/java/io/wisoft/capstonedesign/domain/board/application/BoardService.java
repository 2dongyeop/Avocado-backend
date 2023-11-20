package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.persistence.BoardRepository;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final BoardImageService boardImageService;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long save(final CreateBoardRequest request, final MultipartFile... multipartFiles) {

        // 회원 조회
        final Member member = memberService.findById(request.memberId());
        log.info("member[{}]", member);

        // 게시글 생성
        final Board board = createBoard(request, member);
        log.info("board[{}]", board);
        boardRepository.save(board);

        // 게시글 이미지 저장(로컬)
        boardImageService.save(board.getId(), multipartFiles);

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
        boardRepository.deleteById(boardId);
    }

    /**
     * 게시글 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long boardId, final UpdateBoardRequest request) {

        validateUpdateParam(request);

        final Board board = findById(boardId);
        board.updateTitleBody(request.newTitle(), request.newBody());
    }

    private void validateUpdateParam(final UpdateBoardRequest request) {
        if (!StringUtils.hasText(request.newTitle()) || !StringUtils.hasText(request.newBody())) {
            log.info("parameter is null");
            throw new IllegalValueException("파라미터가 비어있어 게시글을 수정할 수 없습니다.", ErrorCode.ILLEGAL_PARAM);
        }
    }

    /* 조회 로직 */

    /**
     * 게시글 단건 상세 조회
     */
    public Board findDetailById(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.info("boardId[{}] not found", boardId);
            return new NotFoundException("게시글 조회 실패");
        });
    }

    /**
     * 게시글 단건 조회
     */
    public Board findById(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.info("boardId[{}] not found", boardId);
            return new NotFoundException("게시글 조회 실패");
        });
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }


    /**
     * 게시글 목록을 페이징 조회
     */
    public Page<Board> findAllUsingPaging(final Pageable pageable) {
        return boardRepository.findAllUsingPaging(pageable);
    }

    /**
     * 특정 병과의 게시글 목록 페이징 조회
     */
    public Page<Board> findAllByDeptUsingPagingMultiValue(final List<String> deptList, final Pageable pageable) {

        final List<HospitalDept> list = deptList.stream()
                .map(DeptMapper::numberToDept)
                .toList();

        return boardRepository.findAllUsingPagingMultiValue(list, pageable);
    }
}