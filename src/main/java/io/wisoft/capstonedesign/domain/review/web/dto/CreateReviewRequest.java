package io.wisoft.capstonedesign.domain.review.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewRequest {
    private Long memberId;
    private String title;
    private String body;
    private int starPoint;
    private String targetHospital;
    private String photoPath;
}
