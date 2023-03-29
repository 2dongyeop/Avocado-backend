package io.wisoft.capstonedesign.domain.board.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateBoardRequest {
    @NotBlank private String newTitle;
    @NotBlank private String newBody;
}
