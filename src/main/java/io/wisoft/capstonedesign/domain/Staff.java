package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Staff {

    @Id @GeneratedValue()
    @Column(name = "staff_id")
    private Long id;

    @Column(name = "staff_name", nullable = false)
    private String name;

    @Column(name = "staff_email" , unique = true, nullable = false)
    private String email;

    @Column(name = "staff_password", nullable = false)
    private String password;

    @Column(name = "staff_phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "staff_license_path", nullable = false)
    private String license_path;

    @Column(name = "dept", nullable = false)
    private String dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "staff")
    private final List<BoardReply> boardReplyList = new ArrayList<>();

    /* 연관관계 메서드 */
    public void setHospital(Hospital hospital) {
        //comment: 기존 관계 제거
        if (this.hospital != null) {
            this.hospital.getStaffList().remove(this);
        }

        this.hospital = hospital;

        //comment: 무한루프 방지
        if (!hospital.getStaffList().contains(this)) {
            hospital.getStaffList().add(this);
        }
    }

    public void addBoardReply(BoardReply boardReply) {
        this.boardReplyList.add(boardReply);
        if (boardReply.getStaff() != this) { //무한루프에 빠지지 않도록 체크
            boardReply.setStaff(this);
        }
    }
}
