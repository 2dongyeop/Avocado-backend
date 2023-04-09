package io.wisoft.capstonedesign.domain.review.web.dto;

import io.wisoft.capstonedesign.domain.review.persistence.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {
    private String writer;
    private String title;
    private String targetHospital;

    public ReviewListDto(final Review review) {
        this.writer = review.getMember().getNickname();
        this.title = review.getTitle();
        this.targetHospital = review.getTargetHospital();
    }
}
