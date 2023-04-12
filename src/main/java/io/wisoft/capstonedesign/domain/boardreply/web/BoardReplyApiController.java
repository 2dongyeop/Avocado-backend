package io.wisoft.capstonedesign.domain.boardreply.web;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.application.BoardReplyService;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardReplyApiController {

    private final BoardReplyService boardReplyService;

    /* 게시판댓글 저장 */
    @PostMapping("/api/board-reply/new")
    public CreateBoardReplyResponse createBoardReply(
            @RequestBody @Valid final CreateBoardReplyRequest request) {

        Long id = boardReplyService.save(request);
        BoardReply boardReply = boardReplyService.findById(id);
        return new CreateBoardReplyResponse(boardReply.getId());
    }


    /* 게시판댓글 수정 */
    @PatchMapping("/api/board-reply/{id}")
    public UpdateBoardReplyResponse updateBoardReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardReplyRequest request) {

        boardReplyService.update(id, request);
        BoardReply boardReply = boardReplyService.findById(id);
        return new UpdateBoardReplyResponse(boardReply.getId());
    }


    /* 게시판댓글 삭제 */
    @DeleteMapping("/api/board-reply/{id}")
    public DeleteBoardReplyResponse deleteBoardReply(@PathVariable("id") final Long id) {
        boardReplyService.deleteBoardReply(id);
        return new DeleteBoardReplyResponse(id);
    }


    /* 특정 게시판댓글 단건 조회 */
    @GetMapping("/api/board-reply/{id}/details")
    public Result boardReply(@PathVariable("id") final Long id) {
        BoardReply boardReply = boardReplyService.findDetailById(id);

        return new Result(new BoardReplyDto(boardReply));
    }


    /* 특정 게시글의 댓글 목록 오름차순 조회 */
    @GetMapping("/api/board-reply/board/{board-id}/create-asc")
    public Result boardReplyByBoardOrderByCreateAsc(@PathVariable("board-id") final Long id) {
        List<BoardReplyDto> replyDtoList = boardReplyService.findByBoardIdOrderByCreateAsc()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList());

        return new Result(replyDtoList);
    }


    /* 특정 게시글의 댓글 목록 내림차순 조회 */
    @GetMapping("/api/board-reply/board/{board-id}/create-desc")
    public Result boardReplyByBoardOrderByCreateDesc(@PathVariable("board-id") final Long id) {
        List<BoardReplyDto> replyDtoList = boardReplyService.findByBoardIdOrderByCreateDesc()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList());

        return new Result(replyDtoList);
    }
}
