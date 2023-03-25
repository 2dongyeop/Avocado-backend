package io.wisoft.capstonedesign.domain.healthinfo.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateHealthInfoRequest {
    private Long staffId;
    private String title;
    private String dept;
    private String healthInfoPath;
}
