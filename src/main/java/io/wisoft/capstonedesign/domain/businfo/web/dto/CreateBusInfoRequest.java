package io.wisoft.capstonedesign.domain.businfo.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBusInfoRequest {
    private String busInfoPath;
    private String area;
}
