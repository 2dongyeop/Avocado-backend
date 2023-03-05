package io.wisoft.capstonedesign.domain;

import io.wisoft.capstonedesign.domain.enumeration.PickStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Pick {

    @Id @GeneratedValue()
    @Column(name = "pick_id")
    private Long id;

    @Column(name = "pick_date_time")
    private LocalDateTime pickedAt;

    @Column(name = "pick_status")
    @Enumerated(EnumType.STRING)
    private PickStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    /* 연관관계 편의메서드 */
    public void setHospital(Hospital hospital) {
        if (this.hospital != null) {
            this.hospital.getPickList().remove(this);
        }

        this.hospital = hospital;

        //comment: 무한루프 방지
        if (!hospital.getPickList().contains(this)) {
            hospital.getPickList().add(this);
        }
    }

    public void setMember(Member member) {
        //comment: 기존 관계 제거
        if (this.member != null) {
            this.member.getPickList().remove(this);
        }

        this.member = member;

        //comment: 무한루프에 빠지지 않도록 체크
        if (!member.getPickList().contains(this)) {
            member.getPickList().add(this);
        }
    }
}
