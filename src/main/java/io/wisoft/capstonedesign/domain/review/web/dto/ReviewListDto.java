package io.wisoft.capstonedesign.domain.review.web.dto;

import io.wisoft.capstonedesign.domain.review.persistence.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewListDto {
    private Long id;
    private String writer;
    private String title;
    private String status;
    private String targetHospital;
    private String targetDept;
    private int starPoint;

    public ReviewListDto(final Review review) {
        this.id = review.getId();
        this.writer = review.getMember().getNickname();
        this.title = review.getTitle();
        this.status = review.getStatus().toString();
        this.targetHospital = review.getTargetHospital();
        this.targetDept = review.getTargetDept().name();
        this.starPoint = review.getStarPoint();
    }
}
