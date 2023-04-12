package io.wisoft.capstonedesign.domain.board.web;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.board.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    /* 게시글 단건 조회 */
    @GetMapping("/api/boards/{id}/details")
    public Result board(@PathVariable Long id) {
        Board board = boardService.findDetailById(id);

        return new Result(new BoardDto(board));
    }


    /** 게시글 목록 조회 - 페이징 사용 */
    @GetMapping("/api/boards")
    public Page<BoardListDto> boardsUsingPaging(final Pageable pageable) {
        return boardService.findAllUsingPaging(pageable).map(BoardListDto::new);
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
