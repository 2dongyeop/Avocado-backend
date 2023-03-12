package io.wisoft.capstonedesign.domain;

import io.wisoft.capstonedesign.domain.enumeration.BusArea;
import io.wisoft.capstonedesign.domain.enumeration.BusInfoStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "bush_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusInfo {

    @Id @GeneratedValue()
    @Column(name = "bus_info_id")
    private Long id;

    @Column(name = "bus_info_path", nullable = false)
    private String busInfoPath;

    @Column(name = "bus_info_area", nullable = false)
    @Enumerated
    private BusArea area;

    @Column(name = "bus_info_status")
    @Enumerated
    private BusInfoStatus status;

    @Column(name = "bus_info_create_at")
    private LocalDateTime createAt;

    /* 정적 생성 메서드 */
    public static BusInfo createBusInfo(String busInfoPath, BusArea area) {

        BusInfo busInfo = new BusInfo();
        busInfo.busInfoPath = busInfoPath;
        busInfo.area = area;

        busInfo.status = BusInfoStatus.WRITE;
        busInfo.createAt = LocalDateTime.now();

        return busInfo;
    }

    /**
     * 버스정보 삭제
     */
    public void delete() {

        if (this.status == BusInfoStatus.DELETE) {
            throw new IllegalStateException("이미 삭제된 버스정보입니다.");
        }

        this.status = BusInfoStatus.DELETE;
    }
}
