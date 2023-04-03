package io.wisoft.capstonedesign.domain.hospital.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
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
public class Hospital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosp_id")
    private Long id;

    @Column(name = "hosp_name", unique = true, nullable = false)
    private String name;

    @Column(name = "hosp_number", nullable = false)
    private String number;

    @Column(name = "hosp_address", nullable = false)
    private String address;

    @Column(name = "hosp_operatingtime", nullable = false)
    private String operatingTime;

    @OneToMany(mappedBy = "hospital")
    private final List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    private final List<Staff> staffList = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    private final List<Pick> pickList = new ArrayList<>();

    /* 정적 생성 메서드 */
    @Builder
    public static Hospital createHospital(
            final String name,
            final String number,
            final String address,
            final String operatingTime) {

        Hospital hospital = new Hospital();

        hospital.name = name;
        hospital.number = number;
        hospital.address = address;
        hospital.operatingTime = operatingTime;

        return hospital;
    }
}
