package io.wisoft.capstonedesign.domain.boardreply.web.dto;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReplyDto {
    private String boardTitle;
    private String boardWriter;
    private String replyWriter;
    private String reply;

    public BoardReplyDto(final BoardReply boardReply) {
        this.boardTitle = boardReply.getBoard().getTitle();
        this.boardWriter = boardReply.getBoard().getMember().getNickname();
        this.replyWriter = boardReply.getStaff().getName();
        this.reply = boardReply.getReply();
    }
}
