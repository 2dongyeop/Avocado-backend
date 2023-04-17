package io.wisoft.capstonedesign.domain.review.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateReviewRequest(@NotBlank String newTitle, @NotBlank String newBody) { }