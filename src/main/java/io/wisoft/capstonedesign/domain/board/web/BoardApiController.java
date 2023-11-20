package io.wisoft.capstonedesign.domain.board.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.board.application.BoardImageService;
import io.wisoft.capstonedesign.domain.board.application.BoardService;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "게시판")
@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final BoardImageService boardImageService;

    @SwaggerApi(summary = "게시글 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result board(@PathVariable final Long id) {

        log.info("board Id[{}]", id);
        return new Result(new BoardDto(boardService.findDetailById(id)));
    }


    @SwaggerApi(summary = "게시글 목록 조회 및 특정 병과 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping
    public Page<BoardListDto> boardsByDepartmentUsingPaging(
            @RequestParam(required = false) final MultiValueMap<String, String> paramMap, final Pageable pageable) {

        final List<String> deptNumberList = paramMap.get("dept");
        log.info("deptNumberList[{}]", deptNumberList);

        if (deptNumberList == null) {
            return boardService.findAllUsingPaging(pageable).map(BoardListDto::new);
        }

        return boardService.findAllByDeptUsingPagingMultiValue(deptNumberList, pageable).map(BoardListDto::new);
    }


    @SwaggerApi(summary = "게시글 작성", implementation = CreateBoardResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateBoardResponse createBoard(
            @RequestBody final CreateBoardRequest request,
            @RequestParam(value = "image", required = false) final MultipartFile[] multipartFiles) {

        log.info("request[{}]", request);

        final Long id = boardService.save(request, multipartFiles);
        return new CreateBoardResponse(id);
    }


    @SwaggerApi(summary = "게시글 제목 및 본문 수정", implementation = UpdateBoardResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateBoardResponse updateBoardTitleBody(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardRequest request) {

        log.info("board Id[{}], UpdateBoardRequest[{}]", id, request);
        boardService.updateTitleBody(id, request);

        final Board board = boardService.findById(id);
        return new UpdateBoardResponse(board.getId(), board.getTitle(), board.getBody());
    }


    @SwaggerApi(summary = "게시글 삭제", implementation = DeleteBoardResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteBoardResponse deleteBoard(@PathVariable("id") final Long id) {

        log.info("board Id[{}]", id);

        boardService.deleteBoard(id);
        final Board board = boardService.findById(id);
        return new DeleteBoardResponse(board.getId(), board.getStatus().toString());
    }
}
