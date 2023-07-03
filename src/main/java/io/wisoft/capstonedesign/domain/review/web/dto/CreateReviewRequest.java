package io.wisoft.capstonedesign.domain.review.web.dto;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
        @NotNull Long memberId,
        @NotBlank String title,
        @NotBlank String body,
        @NotNull @Min(0) @Max(5) int starPoint,
        @NotBlank String targetHospital,
        @NotBlank String targetDept,
        @NotBlank String photoPath
) { }