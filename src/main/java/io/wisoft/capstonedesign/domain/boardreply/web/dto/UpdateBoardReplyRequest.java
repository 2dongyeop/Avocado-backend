package io.wisoft.capstonedesign.domain.boardreply.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBoardReplyRequest(@NotBlank String reply) { }