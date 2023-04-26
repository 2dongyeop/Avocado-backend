package io.wisoft.capstonedesign.global.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class BlackList {

    @Id @GeneratedValue
    private Long id;

    private String token;

    @Builder
    public BlackList(final Long id, final String token) {
        this.id = id;
        this.token = token;
    }
}
