package io.wisoft.capstonedesign.domain.review.web.dto;

import io.wisoft.capstonedesign.domain.review.persistence.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    private String writer;
    private String title;
    private String body;
    private int starPoint;
    private String targetHospital;

    public ReviewDto(final Review review) {
        this.writer = review.getMember().getNickname();
        this.title = review.getTitle();
        this.body = review.getBody();
        this.starPoint = review.getStarPoint();
        this.targetHospital = review.getTargetHospital();
    }
}
