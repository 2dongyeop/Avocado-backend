package io.wisoft.capstonedesign.domain.board.web.dto;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.BoardReplyDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {
    private String title;
    private String body;
    private String dept;
    private String writer;

    private List<BoardReplyDto> boardReplyList;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BoardDto(final Board board) {
        this.title = board.getTitle();
        this.body = board.getBody();
        this.dept = board.getDept().toString();
        this.writer = board.getMember().getNickname();
        this.createAt = board.getCreatedAt();
        this.updateAt = board.getUpdatedAt();

        this.boardReplyList = board.getBoardReplyList()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList());
    }
}
