package io.wisoft.capstonedesign.domain.pick.web.dto;

import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickDto {
    private String name;
    private String hospital;

    public PickDto(final Pick pick) {
        this.name = pick.getMember().getNickname();
        this.hospital = pick.getHospital().getName();
    }
}
