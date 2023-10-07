package io.wisoft.capstonedesign.domain.member.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.global.config.aes.AESConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Convert(converter = AESConverter.class)
    @Column(name = "member_nickname", unique = true, nullable = false)
    private String nickname;

    @Convert(converter = AESConverter.class)
    @Column(name = "member_email", unique = true, nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Convert(converter = AESConverter.class)
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

        validateParam(nickname, email, password, phoneNumber);

        final Member member = new Member();
        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.phoneNumber = phoneNumber;
        member.createEntity();

        return member;
    }

    private static void validateParam(final String nickname, final String email, final String password, final String phoneNumber) {
        Assert.hasText(nickname, "nickname은 필수입니다.");
        Assert.hasText(email, "email은 필수입니다.");
        Assert.hasText(password, "password는 필수입니다.");
        Assert.hasText(phoneNumber, "phoneNumber는 필수입니다.");
    }


    /**
     * 수정 로직
     */
    public void updatePassword(final String newPassword) {
        this.password = newPassword;
        this.updateEntity();
    }

    public void updateNickname(final String newNickname) {
        this.nickname = newNickname;
        this.updateEntity();
    }

    public void uploadPhotoPath(final String newPhotoPath) {
        this.memberPhotoPath = newPhotoPath;
        this.updateEntity();
    }

}
