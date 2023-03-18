package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.service.BoardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    /* 게시글 조회 */
    @GetMapping("/api/boards")
    public Result boards() {

        List<Board> boardList = boardService.findAllByMemeber();
        List<BoardDto> boardDtoList = boardList.stream()
                .map(board -> new BoardDto(board))
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 게시글 작성 */
    @PostMapping("/api/boards")
    public CreateBoardResponse createBoard(
            @RequestBody @Valid final CreateBoardRequest request) {

        Long id = boardService.save(request.memberId, request.title, request.body, HospitalDept.valueOf(request.dept));

        Board board = boardService.findOne(id);
        return new CreateBoardResponse(board.getId());
    }


    /* 게시글 제목 및 본문 수정 */
    @PatchMapping("/api/boards/{id}")
    public UpdateBoardResponse updateBoardTitleBody(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateBoardRequest request) {

        boardService.updateTitleBody(id, request.newTitle, request.newBody);

        Board board = boardService.findOne(id);
        return new UpdateBoardResponse(board.getId(), board.getTitle(), board.getBody());
    }


    /* 게시글 삭제 */
    @DeleteMapping("/api/boards/{id}")
    public DeleteBoardResponse deleteBoard(
            @PathVariable("id") final Long id) {

        boardService.deleteBoard(id);
        Board board = boardService.findOne(id);
        return new DeleteBoardResponse(board.getId(), board.getStatus().toString());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class BoardDto {
        private String title;
        private String body;
        private String status;
        private String dept;
        private String writer;

        public BoardDto(Board board) {
            this.title = board.getTitle();
            this.body = board.getBody();
            this.status = board.getStatus().toString();
            this.dept = board.getDept().toString();
            this.writer = board.getMember().getNickname();
        }
    }

    @Data
    @AllArgsConstructor
    static class DeleteBoardResponse {
        private Long id;
        private String status;
    }


    @Data
    static class UpdateBoardRequest {
        private String newTitle;
        private String newBody;
    }

    @Data
    @AllArgsConstructor
    static class UpdateBoardResponse {
        private Long id;
        private String newTitle;
        private String newBody;
    }

    @Data
    @AllArgsConstructor
    static class CreateBoardResponse {
        private Long id;
    }

    @Data
    static class CreateBoardRequest {
        private Long memberId;
        private String title;
        private String body;
        private String dept;
        private String boardPhotoPath;
    }
}
