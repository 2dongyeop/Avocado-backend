package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReplyDto {
    private String reviewTitle;
    private String reviewWriter;
    private String replyWriter;
    private String reply;

    public ReviewReplyDto(final ReviewReply reviewReply) {
        this.reviewTitle = reviewReply.getReview().getTitle();
        this.reviewWriter = reviewReply.getReview().getMember().getNickname();
        this.replyWriter = reviewReply.getMember().getNickname();
        this.reply = reviewReply.getReply();
    }
}
