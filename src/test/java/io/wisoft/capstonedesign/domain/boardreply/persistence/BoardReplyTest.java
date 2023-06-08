package io.wisoft.capstonedesign.domain.boardreply.persistence;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardReplyTest {

    @Test
    public void update_success() throws Exception {
        //given -- 조건
        final BoardReply boardReply = getDefaultBoardReply();

        //when -- 동작
        boardReply.update("updateReply");

        //then -- 검증
        Assertions.assertThat(boardReply.getReply()).isEqualTo("updateReply");
    }

    private BoardReply getDefaultBoardReply() {
        return BoardReply.builder()
                .board(Board.builder()
                        .member(Member.builder()
                                .nickname("nickname")
                                .email("email")
                                .password("password")
                                .phoneNumber("phonenumber")
                                .build())
                        .title("boardTitle")
                        .body("boardBody")
                        .dept(HospitalDept.DENTAL)
                        .build())
                .staff(Staff.builder()
                        .hospital(Hospital.builder()
                                .name("hospName")
                                .number("hospNumber")
                                .address("hospAddress")
                                .operatingTime("operatingTime")
                                .build())
                        .name("staffName")
                        .email("staffEmail")
                        .password("staffPassword")
                        .license_path("staffLicense")
                        .dept(HospitalDept.DENTAL)
                        .build())
                .reply("boardReply-body")
                .build();
    }
}