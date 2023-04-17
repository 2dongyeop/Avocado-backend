package io.wisoft.capstonedesign.domain.review.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewsByTargetHospitalRequest(@NotBlank String targetHospital) { }