package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_title", nullable = false)
    private String title;

    @Column(name = "review_body", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "review_createAt", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "review_updateAt", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "review_photo_path", nullable = false)
    private String reviewPhotoPath;

    @Column(name = "star_point", nullable = false)
    private int starPoint;  //TODO: 1~5의 정수만 들어가도록 유효성 검사 추가요망

    @Column(name = "target_hospital", nullable = false)
    private String target_hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<ReviewReply> reviewReplyList = new ArrayList<>();
}
