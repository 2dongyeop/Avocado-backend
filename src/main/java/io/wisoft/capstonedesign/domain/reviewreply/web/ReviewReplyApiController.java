package io.wisoft.capstonedesign.domain.reviewreply.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.reviewreply.application.ReviewReplyService;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰 댓글")
@Slf4j
@RestController
@RequestMapping("/api/review-reply")
@RequiredArgsConstructor
public class ReviewReplyApiController {

    private final ReviewReplyService reviewReplyService;

    @SwaggerApi(summary = "리뷰댓글 저장", implementation = CreateReviewReplyResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateReviewReplyResponse createReviewReply(
            @RequestBody @Valid final CreateReviewReplyRequest request) {

        log.info("CreateReviewReplyRequest[{}]", request);

        final Long id = reviewReplyService.save(request);
        final ReviewReply reviewReply = reviewReplyService.findById(id);
        return new CreateReviewReplyResponse(reviewReply.getId());
    }


    @SwaggerApi(summary = "리뷰댓글 삭제", implementation = DeleteReviewReplyResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteReviewReplyResponse deleteReviewReply(@PathVariable("id") final Long id) {

        log.info("Review Reply Id[{}]", id);

        reviewReplyService.deleteReviewReply(id);
        return new DeleteReviewReplyResponse(id);
    }


    @SwaggerApi(summary = "리뷰댓글 수정", implementation = UpdateReviewReplyResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateReviewReplyResponse updateReviewReply(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateReviewReplyRequest request) {

        log.info("Review Reply Id[{}], UpdateReviewReplyRequest[{}]", id, request);

        reviewReplyService.updateReply(id, request);
        final ReviewReply reviewReply = reviewReplyService.findById(id);
        return new UpdateReviewReplyResponse(reviewReply.getId());
    }


    @SwaggerApi(summary = "리뷰댓글 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result reviewReply(@PathVariable("id") final Long id) {

        log.info("Review Reply Id[{}]", id);
        return new Result(new ReviewReplyDto(reviewReplyService.findDetailById(id)));
    }


    @SwaggerApi(summary = "특정 리뷰의 댓글목록 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/review/{review-id}")
    public Result reviewReplyByReview(
            @PathVariable("review-id") final Long reviewId) {

        log.info("Review Reply Id[{}]", reviewId);

        return new Result(reviewReplyService.findByReviewId(reviewId)
                .stream().map(ReviewReplyDto::new)
                .toList());
    }


    @SwaggerApi(summary = "특정 리뷰의 댓글목록 오름차순 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth

    @GetMapping("/review/{review-id}/create-asc")
    public Result reviewReplyByReviewOrderByCreateAsc(
            @PathVariable("review-id") final Long reviewId) {

        log.info("Review Reply Id[{}]", reviewId);

        return new Result(reviewReplyService.findAllByReviewIdOrderByCreateAsc(reviewId)
                .stream().map(ReviewReplyDto::new)
                .toList());
    }


    @SwaggerApi(summary = "특정 리뷰의 댓글목록 내림차순 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/review/{review-id}/create-desc")
    public Result reviewReplyByReviewOrderByCreateDesc(
            @PathVariable("review-id") final Long reviewId) {

        log.info("Review Reply Id[{}]", reviewId);

        return new Result(reviewReplyService.findAllByReviewIdOrderByCreateDesc(reviewId)
                .stream().map(ReviewReplyDto::new)
                .toList());
    }
}