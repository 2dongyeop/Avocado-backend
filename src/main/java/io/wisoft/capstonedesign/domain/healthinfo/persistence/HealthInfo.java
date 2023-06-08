package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@Table(name = "health_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_info_id")
    private Long id;

    @Column(name = "health_info_path", nullable = false)
    private String healthInfoPath;

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
    @Builder
    public static HealthInfo createHealthInfo(
            final Staff staff,
            final String healthInfoPath,
            final String title,
            final HospitalDept dept) {

        validateParam(staff, healthInfoPath, title, dept);

        final HealthInfo healthInfo = new HealthInfo();
        healthInfo.setStaff(staff);
        healthInfo.healthInfoPath = healthInfoPath;
        healthInfo.title = title;
        healthInfo.dept = dept;

        healthInfo.createEntity();
        return healthInfo;
    }

    private static void validateParam(final Staff staff, final String healthInfoPath, final String title, final HospitalDept dept) {
        Assert.notNull(staff, "staff는 필수입니다.");
        Assert.hasText(healthInfoPath, "healthInfoPath는 필수입니다.");
        Assert.hasText(title, "title은 필수입니다.");
        Assert.notNull(dept, "dept는 필수입니다.");
    }
}
