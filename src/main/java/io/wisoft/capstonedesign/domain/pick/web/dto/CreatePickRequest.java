package io.wisoft.capstonedesign.domain.pick.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePickRequest {
    @NotNull private Long memberId;
    @NotNull private Long hospitalId;
}
