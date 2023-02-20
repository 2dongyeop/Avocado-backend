package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Hospital {

    @Id @GeneratedValue
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
}
