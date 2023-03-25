package io.wisoft.capstonedesign.domain.member.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberRequest {
    private String nickname;
    private String email;
    private String password;
    private String phonenumber;
}
