package io.wisoft.capstonedesign.domain.board.web.dto;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {
    private String title;
    private String body;
    private String status;
    private String dept;
    private String writer;

    public BoardDto(final Board board) {
        this.title = board.getTitle();
        this.body = board.getBody();
        this.status = board.getStatus().toString();
        this.dept = board.getDept().toString();
        this.writer = board.getMember().getNickname();
    }
}
