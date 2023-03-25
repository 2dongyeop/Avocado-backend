package io.wisoft.capstonedesign.domain.boardreply.web.dto;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReplyDto {
    private Long boardId;
    private String boardTitle;
    private String name;
    private String reply;

    public BoardReplyDto(final BoardReply boardReply) {
        this.boardId = boardReply.getBoard().getId();
        this.boardTitle = boardReply.getBoard().getTitle();
        this.name = boardReply.getStaff().getName();
        this.reply = boardReply.getReply();
    }
}
