package io.wisoft.capstonedesign.domain.pick.persistence;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pick extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id")
    private Long id;

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

        pick.createEntity();
        return pick;
    }
}
