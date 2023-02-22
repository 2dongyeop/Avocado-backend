package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email", unique = true, nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_phonenumber", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private final List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<ReviewReply> reviewReplyList = new ArrayList<>();
}
