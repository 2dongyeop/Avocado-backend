package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue()
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "member_email", unique = true, nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "member_photo_path")
    private String memberPhotoPath;

    @OneToMany(mappedBy = "member")
    private final List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<ReviewReply> reviewReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Pick> pickList = new ArrayList<>();


    /* 연관관계 편의 메소드 */
    public void addAppointment(Appointment appointment) {
        this.appointmentList.add(appointment);
        if (appointment.getMember() != this) { //무한루프에 빠지지 않도록 체크
            appointment.setMember(this);
        }
    }

    public void addPick(Pick pick) {
        this.pickList.add(pick);
        if (pick.getMember() != this) { //무한루프에 빠지지 않도록 체크
            pick.setMember(this);
        }
    }

    public void addBoard(Board board) {
        this.boardList.add(board);

        if (board.getMember() != this) {
            board.setMember(this);
        }
    }

    public void addReview(Review review) {
        this.reviewList.add(review);

        if (review.getMember() != this) {
            review.setMember(this);
        }
    }

    public void addReviewReply(ReviewReply reviewReply) {
        this.reviewReplyList.add(reviewReply);

        if (reviewReply.getMember() != this) {
            reviewReply.setMember(this);
        }
    }

    /* 생성 메서드 */
    public static Member newInstance(String nickname, String email, String password, String phoneNumber) {

        Member member = new Member();
        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.phoneNumber = phoneNumber;

        return member;
    }

    /**
     * 수정 로직
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void uploadPhotoPath(String newPhotoPath) {
        this.memberPhotoPath = newPhotoPath;
    }

}
