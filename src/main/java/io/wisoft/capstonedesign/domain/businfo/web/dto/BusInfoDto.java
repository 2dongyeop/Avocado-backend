package io.wisoft.capstonedesign.domain.businfo.web.dto;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusInfoDto {
    private String area;
    private String busInfoPath;

    public BusInfoDto(final BusInfo busInfo) {
        area = busInfo.getArea().toString();
        busInfoPath = busInfo.getBusInfoPath();
    }
}
