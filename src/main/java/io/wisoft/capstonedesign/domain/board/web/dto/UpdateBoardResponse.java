package io.wisoft.capstonedesign.domain.board.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateBoardResponse {
    private Long id;
    private String newTitle;
    private String newBody;
}
