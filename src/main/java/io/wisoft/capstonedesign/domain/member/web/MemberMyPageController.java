package io.wisoft.capstonedesign.domain.member.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.AppointmentDto;
import io.wisoft.capstonedesign.domain.board.web.dto.BoardListDto;
import io.wisoft.capstonedesign.domain.member.application.MemberMyPageService;
import io.wisoft.capstonedesign.domain.pick.web.dto.PickDto;
import io.wisoft.capstonedesign.domain.review.web.dto.ReviewListDto;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "회원 마이페이지")
@RestController
@RequiredArgsConstructor
public class MemberMyPageController {

    private final MemberMyPageService memberMyPageService;

    @SwaggerApi(summary = "자신의 리뷰 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/members/{member-id}/my-page/reviews")
    public Page<ReviewListDto> reviewsByMemberId(
            @PathVariable("member-id") final Long id, final Pageable pageable) {

        log.info("memberId[{}]", id);

        return memberMyPageService.findReviewsByMemberIdUsingPaging(id,pageable)
                .map(ReviewListDto::new);
    }


    @SwaggerApi(summary = "자신이 쓴 게시글 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/members/{member-id}/my-page/boards")
    public Page<BoardListDto> boardsByMemberUsingPaging(
            @PathVariable("member-id") final Long id, final Pageable pageable) {
        log.info("memberId[{}]", id);
        return memberMyPageService.findBoardsByMemberIdUsingPaging(id, pageable).map(BoardListDto::new);
    }


    @SwaggerApi(summary = "자신의 병원 예약 목록 조회", implementation = List.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/members/{member-id}/my-page/appointments")
    public List<AppointmentDto> appointmentsByMemberId(@PathVariable("member-id") final Long memberId) {

        log.info("memberId[{}]", memberId);
        return memberMyPageService.findAppointmentsByMemberId(memberId).stream()
                .map(AppointmentDto::new)
                .toList());
    }


    @SwaggerApi(summary = "자신이 찜한 병원 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/members/{member-id}/my-page/picks")
    public Page<PickDto> picksByMemberId(
            @PathVariable("member-id") final Long memberId, final Pageable pageable) {

        log.info("memberId[{}]", memberId);
        return memberMyPageService.findPicksByMemberIdUsingPaging(memberId, pageable).map(PickDto::new);
    }
}
