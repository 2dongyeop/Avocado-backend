package io.wisoft.capstonedesign.domain.board.web.dto;


public record UpdateBoardResponse(Long id, String newTitle, String newBody) { }