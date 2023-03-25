package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfoRepository;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HealthInfoService {

    private final HealthInfoRepository healthInfoRepository;
    private final StaffService staffService;

    /**
     * 건강정보 저장
     */
    @Transactional
    public Long save(final CreateHealthInfoRequest request) {

        Staff staff = staffService.findOne(request.getStaffId());
        HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, request.getHealthInfoPath(), request.getTitle(), HospitalDept.valueOf(request.getDept()));

        healthInfoRepository.save(healthInfo);
        return healthInfo.getId();
    }

    /**
     * 건강정보 삭제
     */
    @Transactional
    public void delete(final Long healthInfoId) {

        HealthInfo healthInfo = healthInfoRepository.findOne(healthInfoId);
        healthInfo.delete();
    }

    /* 조회 로직 */
    public HealthInfo findOne(final Long healthInfoId) {
        HealthInfo getHealthInfo = healthInfoRepository.findOne(healthInfoId);

        if (getHealthInfo == null) {
            throw new NullHealthInfoException("해당 건강정보가 조회하지 않습니다.");
        }

        return getHealthInfo;
    }

    public List<HealthInfo> findAll() {
        return healthInfoRepository.findAll();
    }

    public List<HealthInfo> findAllByDept(String dept) {

        return healthInfoRepository.findAllByDept(HospitalDept.valueOf(dept));
    }

    public List<HealthInfo> findAllOrderByCreateAsc() {
        return healthInfoRepository.findAllOrderByCreateAsc();
    }

    public List<HealthInfo> findAllOrderByCreateDesc() {
        return healthInfoRepository.findAllOrderByCreateDesc();
    }
}
