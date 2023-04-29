package io.wisoft.capstonedesign.domain.member.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.AppointmentDto;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.board.web.dto.BoardListDto;
import io.wisoft.capstonedesign.domain.member.application.MemberMyPageService;
import io.wisoft.capstonedesign.domain.pick.web.dto.PickDto;
import io.wisoft.capstonedesign.domain.review.web.dto.ReviewListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "회원 마이페이지")
@RestController
@RequiredArgsConstructor
public class MemberMyPageController {

    private final MemberMyPageService memberMyPageService;

    @Operation(summary = "자신의 리뷰 목록 페이징 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/members/{member-id}/my-page/reviews")
    public Page<ReviewListDto> reviewsByMemberId(
            @PathVariable("member-id") final Long id, final Pageable pageable) {

        return memberMyPageService.findReviewsByMemberIdUsingPaging(id,pageable)
                .map(ReviewListDto::new);
    }


    @Operation(summary = "자신이 쓴 게시글 목록 페이징 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/members/{member-id}/my-page/boards")
    public Page<BoardListDto> boardsByMemberUsingPaging(
            @PathVariable("member-id") final Long id, final Pageable pageable) {
        return memberMyPageService.findBoardsByMemberIdUsingPaging(id, pageable).map(BoardListDto::new);
    }


    @Operation(summary = "자신의 병원 예약 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/members/{member-id}/my-page/appointments")
    public List<AppointmentDto> appointmentsByMemberId(@PathVariable("member-id") final Long memberId) {
        return memberMyPageService.findAppointmentsByMemberId(memberId).stream()
                .map(AppointmentDto::new)
                .collect(Collectors.toList());
    }


    @Operation(summary = "자신이 찜한 병원 목록 페이징 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/members/{member-id}/my-page/picks")
    public Page<PickDto> picksByMemberId(
            @PathVariable("member-id") final Long memberId, final Pageable pageable) {

        return memberMyPageService.findPicksByMemberIdUsingPaging(memberId, pageable)
                .map(PickDto::new);
    }
}
