package io.wisoft.capstonedesign.domain.member.web.dto;

import io.wisoft.capstonedesign.domain.appointment.web.dto.AppointmentDto;
import io.wisoft.capstonedesign.domain.board.web.dto.BoardDto;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.web.dto.ReviewDto;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.ReviewReplyDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    private String nickname;
    private String email;
    private String phoneNumber;
    private List<BoardDto> boardList;
    private List<ReviewDto> reviewList;
    private List<ReviewReplyDto> reviewReplyList;
    private List<AppointmentDto> appointmentList;
    private List<Pick> pickList;

    public MemberDto(final Member member) {

        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();

        this.boardList = member.getBoardList()
                .stream().map(BoardDto::new)
                .collect(Collectors.toList());
        this.reviewList = member.getReviewList()
                .stream().map(ReviewDto::new)
                .collect(Collectors.toList());
        this.reviewReplyList = member.getReviewReplyList()
                .stream().map(ReviewReplyDto::new)
                .collect(Collectors.toList());
        this.appointmentList = member.getAppointmentList()
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());
        this.pickList = member.getPickList();
    }
}
