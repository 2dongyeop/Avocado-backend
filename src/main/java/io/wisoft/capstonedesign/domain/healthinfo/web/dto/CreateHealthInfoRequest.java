package io.wisoft.capstonedesign.domain.healthinfo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHealthInfoRequest(
        @NotNull Long staffId,
        @NotBlank String title,
        @NotBlank String dept,
        @NotBlank String healthInfoPath
) { }