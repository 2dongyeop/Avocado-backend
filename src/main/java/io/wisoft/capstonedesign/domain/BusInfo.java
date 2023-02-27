package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BusInfo {

    @Id @GeneratedValue()
    @Column(name = "bus_info_id")
    private Long id;

    @Column(name = "bus_info_path", nullable = false)
    private String busInfoPath;
}
