package io.wisoft.capstonedesign.domain.boardreply.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBoardReplyRequest {
    @NotNull private Long boardId;
    @NotNull private Long staffId;
    @NotBlank private String reply;
}
