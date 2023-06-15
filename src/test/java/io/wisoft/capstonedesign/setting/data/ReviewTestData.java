package io.wisoft.capstonedesign.setting.data;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;

public class ReviewTestData {
    public static Review getDefaultReview(final Member member) {
        return Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .reviewPhotoPath("default-path")
                .target_hospital("아보카도")
                .build();
    }
}
