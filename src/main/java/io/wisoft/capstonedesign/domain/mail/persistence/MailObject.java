package io.wisoft.capstonedesign.domain.mail.persistence;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MailObject {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotNull
    @Size(min = 1, message = "Please, set an email address to send the message to it")
    private String email;
}
