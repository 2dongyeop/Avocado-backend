package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff {

    @Id
    @GeneratedValue()
    @Column(name = "staff_id")
    private Long id;

    @Column(name = "staff_name", nullable = false)
    private String name;

    @Column(name = "staff_email", unique = true, nullable = false)
    private String email;

    @Column(name = "staff_password", nullable = false)
    private String password;

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

    public static Staff newInstance(
            final String name,
            final String email,
            final String password,
            final String license_path,
            final String dept) {
        Staff staff = new Staff();
        staff.name = name;
        staff.email = email;
        staff.password = password;
        staff.license_path = license_path;
        staff.dept = dept;

        return staff;
    }
}
