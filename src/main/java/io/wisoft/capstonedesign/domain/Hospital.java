package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    public void addAppointment(Appointment appointment) {
        this.appointmentList.add(appointment);

        if (appointment.getHospital() != this) { //무한루프에 빠지지 않도록 체크
            appointment.setHospital(this);
        }
    }

    public void addPick(Pick pick) {
        this.pickList.add(pick);

        if (pick.getHospital() != this) { //무한루프에 빠지지 않도록 체크
            pick.setHospital(this);
        }
    }

    public void addStaff(Staff staff) {
        this.staffList.add(staff);

        if (staff.getHospital() != this) {
            staff.setHospital(this);
        }
    }
}
