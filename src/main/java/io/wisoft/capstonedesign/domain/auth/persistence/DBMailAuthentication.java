package io.wisoft.capstonedesign.domain.auth.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DBMailAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private boolean isVerified;

    @Builder
    public DBMailAuthentication(final String email, final boolean isVerified) {
        validateParam(email, isVerified);

        this.email = email;
        this.isVerified = isVerified;
    }

    private void validateParam(final String email, final boolean isVerified) {
        Assert.hasText(email, "email은 필수입니다.");
        Assert.notNull(isVerified, "인증유무는 필수입니다.");
    }

}

