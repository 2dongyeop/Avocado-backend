package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Review;
import io.wisoft.capstonedesign.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    /* 리뷰 조회 */
    @GetMapping("/api/reviews")
    public Result reviews() {

        List<ReviewDto> reviewDtoList = reviewService.findAllByMember().stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());

        return new Result(reviewDtoList);
    }


    /* 리뷰 저장 */
    @PostMapping("/api/reviews")
    public CreateReviewResponse createReview(
            @RequestBody @Valid final CreateReviewRequest request) {

        Long id = reviewService.save(request.memberId, request.title, request.body, request.starPoint, request.targetHospital);
        Review review = reviewService.findOne(id);

        return new CreateReviewResponse(review.getId());
    }


    /* 리뷰 수정 */
    @PatchMapping("/api/reviews/{id}")
    public UpdateReviewResponse updateReview(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateReviewRequest request) {

        reviewService.updateTitleBody(id, request.newTitle, request.newBody);
        Review review = reviewService.findOne(id);

        return new UpdateReviewResponse(review.getId(), review.getTitle(), review.getBody());
    }


    /* 리뷰 삭제 */
    @DeleteMapping("/api/reviews/{id}")
    public DeleteReviewResponse deleteReview(@PathVariable("id") final Long id) {

        reviewService.deleteReview(id);
        Review review = reviewService.findOne(id);

        return new DeleteReviewResponse(review.getId(), review.getStatus().toString());
    }

    @Data
    @AllArgsConstructor
    static class ReviewDto {
        private String writer;
        private String title;
        private String body;
        private int starPoint;
        private String targetHospital;

        public ReviewDto(Review review) {
            this.writer = review.getMember().getNickname();
            this.title = review.getTitle();
            this.body = review.getBody();
            this.starPoint = review.getStarPoint();
            this.targetHospital = review.getTargetHospital();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewResponse {
        private Long id;
    }

    @Data
    static class CreateReviewRequest {
        private Long memberId;
        private String title;
        private String body;
        private int starPoint;
        private String targetHospital;
    }


    @Data
    @AllArgsConstructor
    static class UpdateReviewResponse {
        private Long id;
        private String newTitle;
        private String newBody;
    }

    @Data
    static class UpdateReviewRequest {
        private String newTitle;
        private String newBody;
    }

    @Data
    @AllArgsConstructor
    static class DeleteReviewResponse {
        private Long id;
        private String status;
    }
}
