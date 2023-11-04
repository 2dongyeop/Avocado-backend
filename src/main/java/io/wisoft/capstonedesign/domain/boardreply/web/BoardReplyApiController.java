package io.wisoft.capstonedesign.domain.boardreply.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.boardreply.application.BoardReplyService;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 댓글")
@Slf4j
@RestController
@RequestMapping("/api/board-reply")
@RequiredArgsConstructor
public class BoardReplyApiController {

    private final BoardReplyService boardReplyService;

    @SwaggerApi(summary = "게시판댓글 저장", implementation = CreateBoardReplyResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateBoardReplyResponse createBoardReply(
            @RequestBody @Valid final CreateBoardReplyRequest request) {

        log.info("CreateBoardReplyRequest[{}]", request);

        final Long id = boardReplyService.save(request);
        final BoardReply boardReply = boardReplyService.findById(id);
        return new CreateBoardReplyResponse(boardReply.getId());
    }


    @SwaggerApi(summary = "게시판댓글 수정", implementation = UpdateBoardReplyResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateBoardReplyResponse updateBoardReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardReplyRequest request) {

        log.info("boardReply Id[{}], UpdateBoardReplyRequest[{}]", id, request);

        boardReplyService.update(id, request);
        final BoardReply boardReply = boardReplyService.findById(id);
        return new UpdateBoardReplyResponse(boardReply.getId());
    }


    @SwaggerApi(summary = "게시판댓글 삭제", implementation = DeleteBoardReplyResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteBoardReplyResponse deleteBoardReply(@PathVariable("id") final Long id) {

        log.info("board Reply Id[{}]", id);

        boardReplyService.deleteBoardReply(id);
        return new DeleteBoardReplyResponse(id);
    }


    @SwaggerApi(summary = "특정 게시판댓글 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result boardReply(@PathVariable("id") final Long id) {

        log.info("board Reply Id[{}]", id);
        return new Result(new BoardReplyDto(boardReplyService.findDetailById(id)));
    }


    @SwaggerApi(summary = "특정 게시글의 댓글 목록 오름차순 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/board/{board-id}/create-asc")
    public Result boardReplyByBoardOrderByCreateAsc(@PathVariable("board-id") final Long id) {

        log.info("board Reply Id[{}]", id);

        return new Result(boardReplyService.findByBoardIdOrderByCreateAsc(id)
                .stream().map(BoardReplyDto::new)
                .toList());
    }


    @SwaggerApi(summary = "특정 게시글의 댓글 목록 내림차순 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/board/{board-id}/create-desc")
    public Result boardReplyByBoardOrderByCreateDesc(@PathVariable("board-id") final Long id) {

        log.info("board Reply Id[{}]", id);

        return new Result(boardReplyService.findByBoardIdOrderByCreateDesc(id)
                .stream().map(BoardReplyDto::new)
                .toList());
    }
}
