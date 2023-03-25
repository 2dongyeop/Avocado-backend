package io.wisoft.capstonedesign.domain.member.web.dto;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListDto {
    private String nickname;
    private String email;

    public MemberListDto(final Member member) {
        nickname = member.getNickname();
        email = member.getEmail();
    }
}
