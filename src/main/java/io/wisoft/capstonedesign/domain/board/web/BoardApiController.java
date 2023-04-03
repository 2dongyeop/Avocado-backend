package io.wisoft.capstonedesign.domain.board.web;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.board.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    /* 게시글 단건 조회 */
    @GetMapping("/api/boards/{id}")
    public Result board(@PathVariable Long id) {
        Board board = boardService.findById(id);

        return new Result(new BoardDto(board));
    }


    /* 게시글 조회 */
    @GetMapping("/api/boards")
    public Result boards() {

        List<BoardDto> boardDtoList = boardService.findAllByMember().stream()
                .map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 게시글 생성일자 기준 오름차순 조회 */
    @GetMapping("/api/boards/create-asc")
    public Result boardsOrderByCreateAsc() {

        List<BoardDto> boardDtoList = boardService.findAllOrderByCreateAtAsc()
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 게시글 생성일자 기준 내림차순 조회 */
    @GetMapping("/api/boards/create-desc")
    public Result boardsOrderByCreateDesc() {

        List<BoardDto> boardDtoList = boardService.findAllOrderByCreateAtDesc()
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /** 게시글 오름차순 조회 - 페이징 사용 */
    @GetMapping("/api/boards/{page-number}/create-asc")
    public Result boardsUsingPagingByCreateAsc(@PathVariable("page-number") final int pageNumber) {

        List<BoardDto> boardDtoList = boardService.findAllUsingPagingOrderByCreateAtAsc(pageNumber)
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /** 게시글 내림차순 조회 - 페이징 사용 */
    @GetMapping("/api/boards/{page-number}/create-desc")
    public Result boardsUsingPagingByCreateDesc(@PathVariable("page-number") final int pageNumber) {

        List<BoardDto> boardDtoList = boardService.findAllUsingPagingOrderByCreateAtDesc(pageNumber)
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 특정 작성자의 게시글 목록 조회 */
    @GetMapping("/api/boards/member/{member-id}")
    public Result boardsByMember(@PathVariable("member-id") final Long id) {
        List<BoardDto> boardDtoList = boardService.findByMemberId(id)
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 게시글 작성 */
    @PostMapping("/api/boards/new")
    public CreateBoardResponse createBoard(
            @RequestBody @Valid final CreateBoardRequest request) {

        Long id = boardService.save(request);

        Board board = boardService.findById(id);
        return new CreateBoardResponse(board.getId());
    }


    /* 게시글 제목 및 본문 수정 */
    @PatchMapping("/api/boards/{id}")
    public UpdateBoardResponse updateBoardTitleBody(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardRequest request) {

        boardService.updateTitleBody(id, request);

        Board board = boardService.findById(id);
        return new UpdateBoardResponse(board.getId(), board.getTitle(), board.getBody());
    }


    /* 게시글 삭제 */
    @DeleteMapping("/api/boards/{id}")
    public DeleteBoardResponse deleteBoard(@PathVariable("id") final Long id) {

        boardService.deleteBoard(id);
        Board board = boardService.findById(id);
        return new DeleteBoardResponse(board.getId(), board.getStatus().toString());
    }
}
