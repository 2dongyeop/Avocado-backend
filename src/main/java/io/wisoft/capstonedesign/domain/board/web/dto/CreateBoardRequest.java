package io.wisoft.capstonedesign.domain.board.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequest {
    private Long memberId;
    private String title;
    private String body;
    private String dept;
    private String boardPhotoPath;
}
