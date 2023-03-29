package io.wisoft.capstonedesign.domain.healthinfo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateHealthInfoRequest {
    @NotNull private Long staffId;
    @NotBlank private String title;
    @NotBlank private String dept;
    @NotBlank private String healthInfoPath;
}
