package io.wisoft.capstonedesign.domain.member.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    public void updatePassword_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();

        //when -- 동작
        final String newPassword = "newPassword";
        member.updatePassword(newPassword);

        //then -- 검증
        Assertions.assertThat(newPassword.equals(member.getPassword())).isTrue();
    }


    @Test
    public void updatePhotoPath_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();

        //when -- 동작
        final String newPhotoPath = "newPhotoPath";
        member.uploadPhotoPath(newPhotoPath);

        //then -- 검증
        Assertions.assertThat(member.getMemberPhotoPath().equals(newPhotoPath)).isTrue();
    }


    @Test
    public void updateNickname_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();

        //when -- 동작
        final String newNickname = "newNickname";
        member.updateNickname(newNickname);

        //then -- 검증
        Assertions.assertThat(member.getNickname().equals(newNickname)).isTrue();
    }


    private Member getDefaultMember() {

        return Member.builder()
                .nickname("nickname")
                .email("email")
                .password("password")
                .phoneNumber("phonenumber")
                .build();
    }
}