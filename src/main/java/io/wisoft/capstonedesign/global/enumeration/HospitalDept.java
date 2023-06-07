package io.wisoft.capstonedesign.global.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HospitalDept {
    DENTAL("치과", 1),
    OPHTHALMOLOGY("안과", 2),
    DERMATOLOGY("피부과", 3),
    PLASTIC_SURGERY("성형외과", 4),
    OBSTETRICS("산부인과", 5),
    PSYCHIATRY("정신건강의학과", 6),
    ORTHOPEDICS("정형외과", 7),
    NEUROSURGERY("신경외과", 8),
    SURGICAL("외과", 9),
    NEUROLOGY("신경과", 10),
    PEDIATRIC("소아과", 11),
    INTERNAL_MEDICINE("내과", 12),
    OTOLARYNGOLOGY("이비인후과", 13),
    UROLOGY("비뇨기과", 14),
    ORIENTAL_MEDICAL("한의원", 15);

    private final String description;
    private final int number;

    public String getCode() {
        return name();
    }

    public static List<HospitalDept> toList() {

        return Arrays.stream(HospitalDept.values()).toList();
    }
}
