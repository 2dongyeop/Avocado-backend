package io.wisoft.capstonedesign.boardreply;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
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

        Long id = boardReplyService.save(request.boardId, request.staffId, request.reply);
        BoardReply boardReply = boardReplyService.findOne(id);
        return new CreateBoardReplyResponse(boardReply.getId());
    }


    /* 게시판댓글 수정 */
    @PatchMapping("/api/board-reply/{id}")
    public UpdateBoardReplyResponse updateBoardReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardReplyRequest request) {

        BoardReply boardReply = boardReplyService.findOne(id);
        boardReply.update(request.reply);
        return new UpdateBoardReplyResponse(boardReply.getId());
    }


    /* 게시판댓글 삭제 */
    @DeleteMapping("/api/board-reply/{id}")
    public DeleteBoardReplyResponse deleteBoardReply(@PathVariable("id") final Long id) {
        boardReplyService.deleteBoardReply(id);
        BoardReply boardReply = boardReplyService.findOne(id);
        return new DeleteBoardReplyResponse(boardReply.getId(), boardReply.getStatus().toString());
    }


    /* 특정 게시판댓글 단건 조회 */
    @GetMapping("/api/board-reply/{id}")
    public Result boardReply(@PathVariable("id") final Long id) {
        BoardReply boardReply = boardReplyService.findOne(id);

        return new Result(new BoardReplyDto(boardReply));
    }


    /* 특정 게시글의 댓글 목록 조회 */
    @GetMapping("/api/board-reply/board/{board-id}")
    public Result boardReplyByBoard(@PathVariable("board-id") final Long id) {
        List<BoardReplyDto> replyDtoList = boardReplyService.findByBoardId(id)
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList());

        return new Result(replyDtoList);
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


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class BoardReplyDto {
        private Long boardId;
        private String boardTitle;
        private String name;
        private String reply;

        BoardReplyDto(final BoardReply boardReply) {
            this.boardId = boardReply.getBoard().getId();
            this.boardTitle = boardReply.getBoard().getTitle();
            this.name = boardReply.getStaff().getName();
            this.reply = boardReply.getReply();
        }
    }

    @Data
    @AllArgsConstructor
    static class DeleteBoardReplyResponse {
        private Long id;
        private String status;
    }

    @Data
    @AllArgsConstructor
    static class UpdateBoardReplyResponse {
        private Long id;
    }

    @Data
    static class UpdateBoardReplyRequest {
        private String reply;
    }

    @Data
    @AllArgsConstructor
    static class CreateBoardReplyResponse {
        private Long id;
    }

    @Data
    static class CreateBoardReplyRequest {
        private Long boardId;
        private Long staffId;
        private String reply;
    }
}
