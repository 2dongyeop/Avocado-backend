package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
public class Appointment {

    @Id @GeneratedValue
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
}
