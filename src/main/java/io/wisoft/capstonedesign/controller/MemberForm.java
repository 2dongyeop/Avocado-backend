package io.wisoft.capstonedesign.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 닉네임은 필수입니다.")
    private String nickname;

    @NotEmpty(message = "회원 이메일은 필수입니다.")
    private String email;

    @NotEmpty(message = "회원 비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "회원 연락처는 필수입니다.")
    private String phoneNumber;

    private String memberPhotoPath;
}

