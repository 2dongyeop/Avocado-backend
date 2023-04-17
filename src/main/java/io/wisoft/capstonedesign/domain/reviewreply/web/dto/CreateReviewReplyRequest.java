package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewReplyRequest(
        @NotNull Long memberId,
        @NotNull Long reviewId,
        @NotBlank String reply) { }