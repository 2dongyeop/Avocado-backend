package io.wisoft.capstonedesign.domain.businfo.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BusInfoByAreaRequest(@NotBlank String area) { }
