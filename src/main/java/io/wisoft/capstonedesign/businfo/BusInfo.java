package io.wisoft.capstonedesign.businfo;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.enumeration.status.BusInfoStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bush_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createAt", column = @Column(name = "bus_info_create_at", nullable = false))
public class BusInfo extends BaseEntity {

    @Id @GeneratedValue()
    @Column(name = "bus_info_id")
    private Long id;

    @Column(name = "bus_info_path", nullable = false)
    private String busInfoPath;

    @Column(name = "bus_info_area", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BusArea area;

    @Column(name = "bus_info_status")
    @Enumerated(value = EnumType.STRING)
    private BusInfoStatus status;

    /* 정적 생성 메서드 */
    public static BusInfo createBusInfo(
            final String busInfoPath,
            final BusArea area) {

        BusInfo busInfo = new BusInfo();
        busInfo.busInfoPath = busInfoPath;
        busInfo.area = area;

        busInfo.status = BusInfoStatus.WRITE;
        busInfo.createEntity();

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
