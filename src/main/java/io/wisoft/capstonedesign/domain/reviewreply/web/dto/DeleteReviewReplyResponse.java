package io.wisoft.capstonedesign.domain.reviewreply.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteReviewReplyResponse {
    private Long id;
}
