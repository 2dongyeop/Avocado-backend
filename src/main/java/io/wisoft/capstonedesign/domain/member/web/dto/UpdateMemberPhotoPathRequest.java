package io.wisoft.capstonedesign.domain.member.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberPhotoPathRequest(@NotBlank String photoPath) { }