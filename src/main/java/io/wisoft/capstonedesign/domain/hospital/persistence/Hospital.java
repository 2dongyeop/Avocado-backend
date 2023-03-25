package io.wisoft.capstonedesign.domain.hospital.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

    @Id @GeneratedValue()
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

    /* 연관관계 편의 메서드 */
    public void addAppointment(final Appointment appointment) {
        this.appointmentList.add(appointment);

        if (appointment.getHospital() != this) { //무한루프에 빠지지 않도록 체크
            appointment.setHospital(this);
        }
    }

    public void addPick(final Pick pick) {
        this.pickList.add(pick);

        if (pick.getHospital() != this) { //무한루프에 빠지지 않도록 체크
            pick.setHospital(this);
        }
    }

    public void addStaff(final Staff staff) {
        this.staffList.add(staff);

        if (staff.getHospital() != this) {
            staff.setHospital(this);
        }
    }

    /* 정적 생성 메서드 */
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
