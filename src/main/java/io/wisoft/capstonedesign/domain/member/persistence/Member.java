package io.wisoft.capstonedesign.domain.member.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<ReviewReply> reviewReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Pick> pickList = new ArrayList<>();

    /* 생성 메서드 */
    @Builder
    public static Member newInstance(
            final String nickname,
            final String email,
            final String password,
            final String phoneNumber) {

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
