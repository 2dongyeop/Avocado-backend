package io.wisoft.capstonedesign.setting.data;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;

public class BoardTestData {
    public static Board getDefaultBoard(final Member member) {
        return Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
    }
}
