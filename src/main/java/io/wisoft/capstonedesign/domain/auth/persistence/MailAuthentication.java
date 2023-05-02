package io.wisoft.capstonedesign.domain.auth.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailAuthentication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;
    private boolean isVerified;

    @Builder
    public MailAuthentication(final String email, final String code, final boolean isVerified) {
        this.email = email;
        this.code = code;
        this.isVerified = false;
    }

    public void update() {
        this.isVerified = true;
    }
}
