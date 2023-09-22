package io.wisoft.capstonedesign.domain.review.web.dto;

import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.ReviewReplyDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    private String writer;
    private String title;
    private String body;
    private int starPoint;
    private String targetHospital;
    private List<ReviewReplyDto> reviewReplyList;
    private String targetDept;
    private String photoPath;

    public ReviewDto(final Review review) {
        this.writer = review.getMember().getNickname();
        this.title = review.getTitle();
        this.body = review.getBody();
        this.starPoint = review.getStarPoint();
        this.targetHospital = review.getTargetHospital();
        this.reviewReplyList = review.getReviewReplyList()
                .stream().map(ReviewReplyDto::new)
                .collect(Collectors.toList());
        this.targetDept = String.valueOf(review.getTargetDept());
        this.photoPath = review.getReviewPhotoPath();
    }
}
