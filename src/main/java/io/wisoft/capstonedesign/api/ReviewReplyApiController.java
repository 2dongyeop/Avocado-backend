package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.ReviewReply;
import io.wisoft.capstonedesign.service.ReviewReplyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewReplyApiController {

    private final ReviewReplyService reviewReplyService;


    /* 리뷰댓글 저장 */
    @PostMapping("/api/review-reply/new")
    public CreateReviewReplyResponse createReviewReply(
            @RequestBody @Valid final CreateReviewReplyRequest request) {

        Long id = reviewReplyService.save(request.memberId, request.reviewId, request.reply);
        ReviewReply reviewReply = reviewReplyService.findOne(id);
        return new CreateReviewReplyResponse(reviewReply.getId());
    }


    /* 리뷰댓글 삭제 */
    @DeleteMapping("/api/review-reply/{id}")
    public DeleteReviewReplyResponse deleteReviewReply(
            @PathVariable("id") final Long id) {

        reviewReplyService.deleteReviewReply(id);
        ReviewReply reviewReply = reviewReplyService.findOne(id);
        return new DeleteReviewReplyResponse(reviewReply.getId(), reviewReply.getStatus().toString());
    }


    /* 리뷰댓글 수정 */
    @PatchMapping("/api/review-reply/{id}")
    public UpdateReviewReplyResponse updateReviewReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateReviewReplyRequest request) {

        reviewReplyService.updateReply(id, request.reply);
        ReviewReply reviewReply = reviewReplyService.findOne(id);
        return new UpdateReviewReplyResponse(reviewReply.getId());
    }


    /* 리뷰댓글 단건 조회 */
    @GetMapping("/api/review-reply/{id}")
    public Result reviewReply(@PathVariable("id") final Long id) {

        ReviewReply reviewReply = reviewReplyService.findOne(id);
        return new Result(new ReviewReplyDto(reviewReply));
    }


    /* 특정 리뷰의 댓글목록 조회 */
    @GetMapping("/api/review-reply/review/{review-id}")
    public Result reviewReplyByReview(
            @PathVariable("review-id") final Long reviewId) {

        List<ReviewReplyDto> reviewReplyDtoList = reviewReplyService.findByReviewId(reviewId)
                .stream().map(ReviewReplyDto::new)
                .collect(Collectors.toList());

        return new Result(reviewReplyDtoList);
    }


    /* 특정 리뷰의 댓글목록 오름차순 조회 */
    @GetMapping("/api/review-reply/review/{review-id}/create-asc")
    public Result reviewReplyByReviewOrderByCreateAsc(
            @PathVariable("review-id") final Long reviewId) {

        List<ReviewReplyDto> reviewReplyDtoList = reviewReplyService.findAllByReviewIdOrderByCreateAsc(reviewId)
                .stream().map(ReviewReplyDto::new)
                .collect(Collectors.toList());

        return new Result(reviewReplyDtoList);
    }


    /* 특정 리뷰의 댓글목록 내림차순 조회 */
    @GetMapping("/api/review-reply/review/{review-id}/create-desc")
    public Result reviewReplyByReviewOrderByCreateDesc(
            @PathVariable("review-id") final Long reviewId) {

        List<ReviewReplyDto> reviewReplyDtoList = reviewReplyService.findAllByReviewIdOrderByCreateDesc(reviewId)
                .stream().map(ReviewReplyDto::new)
                .collect(Collectors.toList());

        return new Result(reviewReplyDtoList);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class ReviewReplyDto {
        private Long reviewId;
        private String reviewTitle;
        private String writer;
        private String reply;

        public ReviewReplyDto(final ReviewReply reviewReply) {
            this.reviewId = reviewReply.getReview().getId();
            this.reviewTitle = reviewReply.getReview().getTitle();
            this.writer = reviewReply.getMember().getNickname();
            this.reply = reviewReply.getReply();
        }
    }


    @Data
    @AllArgsConstructor
    static class UpdateReviewReplyResponse {
        private Long id;
    }

    @Data
    static class UpdateReviewReplyRequest {
        private String reply;
    }

    @Data
    @AllArgsConstructor
    static class DeleteReviewReplyResponse {
        private Long id;
        private String status;
    }

    @Data
    static class CreateReviewReplyRequest {
        private Long memberId;
        private Long reviewId;
        private String reply;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewReplyResponse {
        private Long id;
    }
}