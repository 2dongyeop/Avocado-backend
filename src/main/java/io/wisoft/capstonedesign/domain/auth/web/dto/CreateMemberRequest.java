package io.wisoft.capstonedesign.domain.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberRequest {
    @NotBlank private String nickname;

    @NotBlank @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
            message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
    @NotBlank private String password1;

    @NotBlank private String password2;
    @NotBlank private String phonenumber;
    @NotBlank private String code;
}
