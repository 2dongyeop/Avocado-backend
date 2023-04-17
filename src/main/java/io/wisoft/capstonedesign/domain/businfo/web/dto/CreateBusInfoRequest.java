package io.wisoft.capstonedesign.domain.businfo.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBusInfoRequest(
        @NotBlank String busInfoPath,
        @NotBlank String area
) { }
