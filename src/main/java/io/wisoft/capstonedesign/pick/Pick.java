package io.wisoft.capstonedesign.pick;

import io.wisoft.capstonedesign.global.enumeration.status.PickStatus;
import io.wisoft.capstonedesign.hospital.Hospital;
import io.wisoft.capstonedesign.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    public void setHospital(final Hospital hospital) {
        if (this.hospital != null) {
            this.hospital.getPickList().remove(this);
        }

        this.hospital = hospital;

        //comment: 무한루프 방지
        if (!hospital.getPickList().contains(this)) {
            hospital.getPickList().add(this);
        }
    }

    public void setMember(final Member member) {
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

    /* 정적 생성 메서드 */
    public static Pick createPick(final Member member, final Hospital hospital) {
        Pick pick = new Pick();
        pick.setMember(member);
        pick.setHospital(hospital);
        pick.pickedAt = LocalDateTime.now();
        pick.status = PickStatus.COMPLETE;

        return pick;
    }

    /**
     * 찜하기 취소
     */
    public void cancel() {

        if (this.status == PickStatus.CANCEL) {
            throw new IllegalStateException("이미 취소된 찜하기입니다.");
        }

        this.status = PickStatus.CANCEL;
    }
}
