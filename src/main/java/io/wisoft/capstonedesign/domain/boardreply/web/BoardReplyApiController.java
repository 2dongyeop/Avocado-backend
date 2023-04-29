package io.wisoft.capstonedesign.domain.boardreply.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.application.BoardReplyService;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "게시판 댓글")
@RestController
@RequiredArgsConstructor
public class BoardReplyApiController {

    private final BoardReplyService boardReplyService;

    @Operation(summary = "게시판댓글 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/board-reply/new")
    public CreateBoardReplyResponse createBoardReply(
            @RequestBody @Valid final CreateBoardReplyRequest request) {

        final Long id = boardReplyService.save(request);
        final BoardReply boardReply = boardReplyService.findById(id);
        return new CreateBoardReplyResponse(boardReply.getId());
    }


    @Operation(summary = "게시판댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PatchMapping("/api/board-reply/{id}")
    public UpdateBoardReplyResponse updateBoardReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardReplyRequest request) {

        boardReplyService.update(id, request);
        final BoardReply boardReply = boardReplyService.findById(id);
        return new UpdateBoardReplyResponse(boardReply.getId());
    }


    @Operation(summary = "게시판댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/api/board-reply/{id}")
    public DeleteBoardReplyResponse deleteBoardReply(@PathVariable("id") final Long id) {
        boardReplyService.deleteBoardReply(id);
        return new DeleteBoardReplyResponse(id);
    }


    @Operation(summary = "특정 게시판댓글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/board-reply/{id}/details")
    public Result boardReply(@PathVariable("id") final Long id) {
        return new Result(new BoardReplyDto(boardReplyService.findDetailById(id)));
    }


    @Operation(summary = "특정 게시글의 댓글 목록 오름차순 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/board-reply/board/{board-id}/create-asc")
    public Result boardReplyByBoardOrderByCreateAsc(@PathVariable("board-id") final Long id) {
        return new Result(boardReplyService.findByBoardIdOrderByCreateAsc()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList()));
    }


    @Operation(summary = "특정 게시글의 댓글 목록 내림차순 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/board-reply/board/{board-id}/create-desc")
    public Result boardReplyByBoardOrderByCreateDesc(@PathVariable("board-id") final Long id) {
        return new Result(boardReplyService.findByBoardIdOrderByCreateDesc()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList()));
    }
}
