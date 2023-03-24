package io.wisoft.capstonedesign.healthinfo;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.staff.Staff;
import io.wisoft.capstonedesign.global.enumeration.status.HealthInfoStatus;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "health_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createAt", column = @Column(name = "health_info_create_at", nullable = false))
public class HealthInfo extends BaseEntity {

    @Id @GeneratedValue()
    @Column(name = "health_info_id")
    private Long id;

    @Column(name = "health_info_path", nullable = false)
    private String healthInfoPath;

    @Column(name = "health_info_status")
    @Enumerated(value = EnumType.STRING)
    private HealthInfoStatus status;

    @Column(name = "health_info_title", nullable = false)
    private String title;

    @Column(name = "dept")
    @Enumerated(value = EnumType.STRING)
    private HospitalDept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    /* 연관관계 편의 메서드 */
    public void setStaff(final Staff staff) {
        if (this.staff != null) {
            this.staff.getHealthInfoList().remove(this);
        }

        this.staff = staff;
        if (!staff.getHealthInfoList().contains(this)) {
            staff.getHealthInfoList().add(this);
        }
    }

    /* 정적 생성 메서드 */
    public static HealthInfo createHealthInfo(
            final Staff staff,
            final String healthInfoPath,
            final String title,
            final HospitalDept dept) {

        HealthInfo healthInfo = new HealthInfo();
        healthInfo.setStaff(staff);
        healthInfo.healthInfoPath = healthInfoPath;
        healthInfo.title = title;
        healthInfo.dept = dept;

        healthInfo.status = HealthInfoStatus.WRITE;
        healthInfo.createEntity();

        return healthInfo;
    }

    /**
     * 건강정보 삭제
     */
    public void delete() {

        if (this.status == HealthInfoStatus.DELETE) {
            throw new IllegalStateException("이미 삭제된 건강정보입니다.");
        }

        this.status = HealthInfoStatus.DELETE;
    }
}
