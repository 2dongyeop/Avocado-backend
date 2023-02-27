package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Appointment {

    @Id @GeneratedValue()
    @Column(name = "appointment_id")
    private Long id;

    @CreatedDate
    @Column(name = "appt_date_time", nullable = false)
    private LocalDateTime appointedAt;

    @Column(name = "appt_dept", nullable = false)
    private String dept;

    @Column(name = "appt_comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    /* 연관관계 편의메서드 */
    public void setHospital(Hospital hospital) {
        //comment: 기존 관계 제거
        if (this.hospital != null) {
            this.hospital.getAppointmentList().remove(this);
        }

        this.hospital = hospital;

        //comment: 무한루프 방지
        if (!hospital.getAppointmentList().contains(this)) {
            hospital.getAppointmentList().add(this);
        }
    }

    public void setMember(Member member) {
        //comment: 기존 관계 제거
        if (this.member != null) {
            this.member.getAppointmentList().remove(this);
        }

        this.member = member;

        //comment: 무한루프에 빠지지 않도록 체크
        if (!member.getAppointmentList().contains(this)) {
            member.getAppointmentList().add(this);
        }
    }
}
