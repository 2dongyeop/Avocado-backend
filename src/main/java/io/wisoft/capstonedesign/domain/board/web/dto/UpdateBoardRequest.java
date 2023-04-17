package io.wisoft.capstonedesign.domain.board.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBoardRequest(
        @NotBlank String newTitle,
        @NotBlank String newBody
) { }