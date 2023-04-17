package io.wisoft.capstonedesign.domain.boardreply.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoardReplyRequest(
        @NotNull Long boardId,
        @NotNull Long staffId,
        @NotBlank String reply
) { }