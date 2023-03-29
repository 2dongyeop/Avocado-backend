package io.wisoft.capstonedesign.domain.businfo.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBusInfoRequest {
    @NotBlank private String busInfoPath;
    @NotBlank private String area;
}
