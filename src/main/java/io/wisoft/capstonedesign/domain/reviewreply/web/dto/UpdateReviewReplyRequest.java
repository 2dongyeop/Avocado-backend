package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateReviewReplyRequest(@NotBlank String reply) { }