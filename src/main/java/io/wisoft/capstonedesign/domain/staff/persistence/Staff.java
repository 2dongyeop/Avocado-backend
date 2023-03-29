package io.wisoft.capstonedesign.domain.staff.persistence;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
    private final List<BoardReply> boardReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
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

    /* 정적 생성 메서드 */
    @Builder
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
}
