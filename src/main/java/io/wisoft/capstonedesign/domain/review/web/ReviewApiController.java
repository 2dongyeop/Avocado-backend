package io.wisoft.capstonedesign.domain.review.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.application.ReviewService;
import io.wisoft.capstonedesign.domain.review.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Tag(name = "리뷰")
@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @SwaggerApi(summary = "리뷰 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result review(@PathVariable("id") final Long id) {

        log.debug("review Id[{}]", id);
        return new Result(new ReviewDto(reviewService.findDetailById(id)));
    }


    @SwaggerApi(summary = "리뷰 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping
    public Page<ReviewListDto> reviewsUsingPaging(
            final Pageable pageable,
            @RequestParam(required = false) final String deptNum) {

        log.debug("deptNum[{}]", deptNum);

        if (StringUtils.hasText(deptNum)) {
            return reviewService.findByDeptUsingPaging(deptNum, pageable).map(ReviewListDto::new);
        }

        return reviewService.findByUsingPaging(pageable).map(ReviewListDto::new);
    }


    @SwaggerApi(summary = "특정 병원의 리뷰 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/hospital")
    public Page<ReviewListDto> reviewsByTargetHospital(
            @RequestBody @Valid final ReviewsByTargetHospitalRequest request,
            final Pageable pageable) {

        log.debug("ReviewsByTargetHospitalRequest[{}]", request);

        return reviewService.findByTargetHospital(request.targetHospital(), pageable)
                .map(ReviewListDto::new);
    }


    @SwaggerApi(summary = "리뷰 저장", implementation = CreateReviewResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateReviewResponse createReview(
            @RequestBody @Valid final CreateReviewRequest request) {

        log.debug("CreateReviewRequest[{}]", request);

        final Long id = reviewService.save(request);
        final Review review = reviewService.findById(id);

        return new CreateReviewResponse(review.getId());
    }


    @SwaggerApi(summary = "리뷰 수정", implementation = UpdateReviewResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateReviewResponse updateReview(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateReviewRequest request) {

        log.debug("review Id[{}], UpdateReviewRequest[{}]", id, request);

        reviewService.updateTitleBody(id, request);
        final Review review = reviewService.findById(id);

        return new UpdateReviewResponse(review.getId(), review.getTitle(), review.getBody());
    }


    @SwaggerApi(summary = "리뷰 삭제", implementation = DeleteReviewResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteReviewResponse deleteReview(@PathVariable("id") final Long id) {

        log.debug("review Id[{}]", id);

        reviewService.deleteReview(id);
        final Review review = reviewService.findById(id);

        return new DeleteReviewResponse(review.getId(), review.getStatus().toString());
    }
}
