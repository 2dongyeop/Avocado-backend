package io.wisoft.capstonedesign.global.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusArea {
    DAEJEON("대전"),
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    JEJU("제주");

    private final String area;

    public String getCode() {
        return name();
    }
}
