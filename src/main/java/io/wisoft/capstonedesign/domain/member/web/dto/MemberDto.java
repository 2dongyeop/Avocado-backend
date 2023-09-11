package io.wisoft.capstonedesign.domain.member.web.dto;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    private String nickname;
    private String email;
    private String phoneNumber;
    private List<BoardDto> boardList;
    private List<ReviewDto> reviewList;
    private List<AppointmentDto> appointmentList;

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
        this.appointmentList = member.getAppointmentList()
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    static class BoardDto {

        private String title;
        private String body;
        private String dept;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public BoardDto(final Board board) {
            this.title = board.getTitle();
            this.body = board.getBody();
            this.dept = board.getDept().toString();
            this.createAt = board.getCreatedAt();
            this.updateAt = board.getUpdatedAt();
        }
    }

    @Getter
    static class ReviewDto {
        private String title;
        private String body;
        private int starPoint;
        private String targetHospital;

        public ReviewDto(final Review review) {
            this.title = review.getTitle();
            this.body = review.getBody();
            this.starPoint = review.getStarPoint();
            this.targetHospital = review.getTargetHospital();
        }
    }

    @Getter
    static class AppointmentDto {
        private String hospital;
        private String dept;
        private String comment;
        private String appointName;
        private String appointPhonenumber;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

        public AppointmentDto(final Appointment appointment) {
            this.hospital = appointment.getHospital().getName();
            this.dept = appointment.getDept().toString();
            this.comment = appointment.getComment();
            this.appointName = appointment.getAppointName();
            this.appointPhonenumber = appointment.getAppointPhonenumber();
            this.createAt = appointment.getCreatedAt();
            this.updateAt = appointment.getUpdatedAt();
        }
    }
}
