package io.wisoft.capstonedesign.domain.pick.web.dto;

import jakarta.validation.constraints.NotNull;

public record CreatePickRequest(
        @NotNull Long memberId,
        @NotNull Long hospitalId
) { }
