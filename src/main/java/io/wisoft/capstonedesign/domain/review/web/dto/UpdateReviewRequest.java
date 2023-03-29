package io.wisoft.capstonedesign.domain.review.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateReviewRequest {
    @NotBlank private String newTitle;
    @NotBlank private String newBody;
}
