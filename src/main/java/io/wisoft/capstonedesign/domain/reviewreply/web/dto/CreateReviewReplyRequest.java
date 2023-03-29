package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewReplyRequest {
    @NotNull private Long memberId;
    @NotNull private Long reviewId;
    @NotBlank private String reply;
}
