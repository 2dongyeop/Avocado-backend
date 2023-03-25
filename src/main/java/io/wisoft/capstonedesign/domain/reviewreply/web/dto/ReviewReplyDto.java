package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReplyDto {
    private Long reviewId;
    private String reviewTitle;
    private String writer;
    private String reply;

    public ReviewReplyDto(final ReviewReply reviewReply) {
        this.reviewId = reviewReply.getReview().getId();
        this.reviewTitle = reviewReply.getReview().getTitle();
        this.writer = reviewReply.getMember().getNickname();
        this.reply = reviewReply.getReply();
    }
}
