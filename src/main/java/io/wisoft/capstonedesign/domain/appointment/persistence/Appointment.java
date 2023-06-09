package io.wisoft.capstonedesign.domain.appointment.persistence;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @Column(name = "appt_dept", nullable = false)
    @Enumerated(EnumType.STRING)
    private HospitalDept dept;

    @Column(name = "appt_comment", nullable = false, columnDefinition="TEXT")
    private String comment;

    @Column(name = "appt_name", nullable = false)
    private String appointName;

    @Column(name = "appt_phonenumber", nullable = false)
    private String appointPhonenumber;

    @Column(name = "appt_pay_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @Column(name = "appt_date", nullable = false)
    private LocalDateTime appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    /* 연관관계 편의메서드 */
    public void setHospital(final Hospital hospital) {
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

    public void setMember(final Member member) {
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

    /* 정적 생성 메서드 */
    @Builder
    public static Appointment createAppointment(
            final Member member,
            final Hospital hospital,
            final HospitalDept dept,
            final String comment,
            final String appointName,
            final String appointPhonenumber,
            final PayStatus payStatus,
            final LocalDateTime appointmentDate) {

        validateParam(member, hospital, dept, comment, appointName, appointPhonenumber, payStatus, appointmentDate);

        final Appointment appointment = new Appointment();
        appointment.setMember(member);
        appointment.setHospital(hospital);
        appointment.dept = dept;
        appointment.comment = comment;
        appointment.appointName = appointName;
        appointment.appointPhonenumber = appointPhonenumber;
        appointment.payStatus = payStatus;
        appointment.appointmentDate = appointmentDate;

        appointment.createEntity();
        return appointment;
    }

    private static void validateParam(final Member member, final Hospital hospital, final HospitalDept dept, final String comment, final String appointName, final String appointPhonenumber, final PayStatus payStatus, final LocalDateTime appointmentDate) {
        Assert.notNull(member, "member는 필수입니다.");
        Assert.notNull(hospital, "hospital는 필수입니다.");
        Assert.notNull(dept, "dept는 필수입니다.");
        Assert.hasText(comment, "comment는 필수입니다.");
        Assert.hasText(appointName, "appointment는 필수입니다.");
        Assert.hasText(appointPhonenumber, "appointPhonenumber는 필수입니다.");
        Assert.notNull(payStatus, "payStatus는 필수입니다.");
        Assert.notNull(appointmentDate, "예약 날짜는 필수입니다.");
    }

    /**
     * 예약 수정
     */
    public void update(
            final HospitalDept dept,
            final String comment,
            final String appointName,
            final String appointPhonenumber) {
        this.dept = dept;
        this.comment = comment;
        this.appointName = appointName;
        this.appointPhonenumber = appointPhonenumber;

        this.updateEntity();
    }

    public void payment() {
    if (this.payStatus == PayStatus.COMPLETED) {
            throw new IllegalValueException("이미 결제된 예약건입니다.", ErrorCode.ILLEGAL_STATE);
        }

        this.payStatus = PayStatus.COMPLETED;
    }

    public void refund() {
        if (this.payStatus == PayStatus.REFUND) {
            throw new IllegalValueException("이미 환불된 예약건입니다.", ErrorCode.ILLEGAL_STATE);
        }

        this.payStatus = PayStatus.REFUND;
    }
}
