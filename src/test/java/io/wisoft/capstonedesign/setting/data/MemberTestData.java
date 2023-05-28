package io.wisoft.capstonedesign.setting.data;

import io.wisoft.capstonedesign.domain.member.persistence.Member;

public class MemberTestData {
    public static Member getDefaultMember() {
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        return member;
    }
}
