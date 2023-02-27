package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "health_info")
public class HealthInfo {

    @Id @GeneratedValue()
    @Column(name = "health_info_id")
    private Long id;

    @Column(name = "health_info_path", nullable = false)
    private String healthInfoPath;
}
