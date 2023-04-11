package io.wisoft.capstonedesign.domain.businfo.persistence;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bus_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_info_id")
    private Long id;

    @Column(name = "bus_info_path", nullable = false)
    private String busInfoPath;

    @Column(name = "bus_info_area", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BusArea area;

    /* 정적 생성 메서드 */
    public static BusInfo createBusInfo(
            final String busInfoPath,
            final BusArea area) {

        BusInfo busInfo = new BusInfo();
        busInfo.busInfoPath = busInfoPath;
        busInfo.area = area;

        busInfo.createEntity();
        return busInfo;
    }
}
