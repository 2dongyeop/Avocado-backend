package io.wisoft.capstonedesign.domain.staff.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateStaffRequest {
    private Long hospitalId;
    private String name;
    private String email;
    private String password;
    private String licensePath;
    private String dept;
}
