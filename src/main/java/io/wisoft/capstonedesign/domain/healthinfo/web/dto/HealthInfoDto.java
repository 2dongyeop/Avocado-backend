package io.wisoft.capstonedesign.domain.healthinfo.web.dto;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthInfoDto {
    private String writer;
    private String title;
    private String dept;
    private String healthInfoPath;

    public HealthInfoDto(final HealthInfo healthInfo) {
        this.writer = healthInfo.getStaff().getName();
        this.title = healthInfo.getTitle();
        this.dept = healthInfo.getDept().toString();
        this.healthInfoPath = healthInfo.getHealthInfoPath();
    }
}
