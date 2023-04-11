package io.wisoft.capstonedesign.domain.board.web.dto;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardListDto {

    private String title;
    private String body;
    private String dept;
    private String writer;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BoardListDto(Board board) {
        this.title = board.getTitle();
        this.body = board.getBody();
        this.dept = board.getDept().toString();
        this.writer = board.getMember().getNickname();
        this.status = board.getStatus().toString();
        this.createAt = board.getCreatedAt();
        this.updateAt = board.getUpdatedAt();
    }
}
