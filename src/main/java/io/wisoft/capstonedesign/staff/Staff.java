package io.wisoft.capstonedesign.staff;

import io.wisoft.capstonedesign.boardreply.BoardReply;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.StaffStatus;
import io.wisoft.capstonedesign.healthinfo.HealthInfo;
import io.wisoft.capstonedesign.hospital.Hospital;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "dept", nullable = false)
    private HospitalDept dept;

    @Column(name = "staff_photo_path")
    private String staffPhotoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    private StaffStatus status;

    @OneToMany(mappedBy = "staff")
    private final List<BoardReply> boardReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "staff")
    private final List<HealthInfo> healthInfoList = new ArrayList<>();

    /* 연관관계 메서드 */
    public void setHospital(final Hospital hospital) {
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

    public void addBoardReply(final BoardReply boardReply) {
        this.boardReplyList.add(boardReply);
        if (boardReply.getStaff() != this) { //무한루프에 빠지지 않도록 체크
            boardReply.setStaff(this);
        }
    }

    public void addHealthInfo(final HealthInfo healthInfo) {
        this.healthInfoList.add(healthInfo);
        if (healthInfo.getStaff() != this) {
            healthInfo.setStaff(this);
        }
    }

    /* 정적 생성 메서드 */
    public static Staff newInstance(
            final Hospital hospital,
            final String name,
            final String email,
            final String password,
            final String license_path,
            final HospitalDept dept) {

        Staff staff = new Staff();
        staff.setHospital(hospital);
        staff.name = name;
        staff.email = email;
        staff.password = password;
        staff.license_path = license_path;
        staff.dept = dept;
        staff.status = StaffStatus.SAVE;
        return staff;
    }


    /*
     * 의료진 비밀번호 수정
     * */
    public void updatePassword(final String newPassword) {
        this.password = newPassword;
    }


    /*
     * 의료진 프로필사진 수정
     */
    public void updatePhotoPath(final String newPhotoPath) {
        this.staffPhotoPath = newPhotoPath;
    }

    /* 의료진 병원 수정 */
    public void updateHospital(final Hospital hospital) {

        setHospital(hospital);
    }

    public void delete() {

        if (this.status == StaffStatus.DELETE) {
            throw new IllegalStateException("이미 탈퇴된 의료진입니다.");
        }
        this.status = StaffStatus.DELETE;
    }
}
