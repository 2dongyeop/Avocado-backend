package io.wisoft.capstonedesign.domain.board.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequest {
    @NotNull private Long memberId;
    @NotBlank private String title;
    @NotBlank private String body;
    @NotBlank private String dept;
    @NotBlank private String boardPhotoPath;
}
