package io.wisoft.capstonedesign.domain.staff.web;

import io.wisoft.capstonedesign.domain.board.web.dto.BoardDto;
import io.wisoft.capstonedesign.domain.review.web.dto.ReviewDto;
import io.wisoft.capstonedesign.domain.staff.application.StaffMyPageService;
import io.wisoft.capstonedesign.domain.staff.web.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StaffMyPageController {

    private final StaffMyPageService staffMyPageService;

    /** 자신이 속한 병원의 리뷰 목록 조회 */
    @GetMapping("/api/staff/{staff-id}/my-page/reviews")
    public Result reviewListByStaffId(@PathVariable("staff-id") final Long id) {
        List<ReviewDto> reviewDtoList = staffMyPageService.findReviewByStaffHospitalName(id)
                .stream().map(ReviewDto::new)
                .collect(Collectors.toList());
        return new Result(reviewDtoList);
    }


    /** 자신이 댓글을 작성한 게시글 목록 조회 */
    @GetMapping("/api/staff/{staff-id}/my-page/boards")
    public Result boardListByStaffId(@PathVariable("staff-id") final Long id) {
        return new Result(staffMyPageService.findBoardListByStaffId(id).stream()
                .map(BoardDto::new)
                .collect(Collectors.toList()));
    }
}
