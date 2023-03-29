package io.wisoft.capstonedesign.domain.review.web.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewRequest {
    @NotNull private Long memberId;
    @NotBlank private String title;
    @NotBlank private String body;
    @NotNull
    @Min(0) @Max(5)
    private int starPoint;
    @NotBlank private String targetHospital;
    @NotBlank private String photoPath;
}
