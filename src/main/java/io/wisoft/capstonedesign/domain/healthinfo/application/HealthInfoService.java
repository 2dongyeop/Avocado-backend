package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfoRepository;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Iterator;
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

        Staff staff = staffService.findById(request.getStaffId());

        HealthInfo healthInfo = HealthInfo.builder()
                .staff(staff)
                .healthInfoPath(request.getHealthInfoPath())
                .title(request.getTitle())
                .dept(HospitalDept.valueOf(request.getDept()))
                .build();

        healthInfoRepository.save(healthInfo);
        return healthInfo.getId();
    }

    /**
     * 건강정보 삭제
     */
    @Transactional
    public void delete(final Long healthInfoId) {
        HealthInfo healthInfo = findById(healthInfoId);
        healthInfoRepository.delete(healthInfo);
    }

    /* 조회 로직 */
    public HealthInfo findById(final Long healthInfoId) {
        return healthInfoRepository.findById(healthInfoId).orElseThrow(NullHealthInfoException::new);
    }

    public HealthInfo findDetailById(final Long healthInfoId) {
        return healthInfoRepository.findDetailById(healthInfoId).orElseThrow(NullHealthInfoException::new);
    }

    public List<HealthInfo> findAll() {
        return healthInfoRepository.findAll();
    }


    /** 특정 병과의 건강정보 목록을 페이지별로 조회하기 */
    public Page<HealthInfo> findAllByDeptUsingPaging(final String dept, final Pageable pageable) {

        validateDept(dept);
        return healthInfoRepository.findAllByDeptUsingPaging(HospitalDept.valueOf(dept), pageable);
    }

    private boolean validateDept(final String dept) {

        Iterator<HospitalDept> iterator = Arrays.stream(HospitalDept.values()).iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getCode().equals(dept.toUpperCase())) {
                return true;
            }
        }
        throw new IllegalValueException("일치하는 hospitalDept가 없습니다.");
    }

    /** 건강정보 목록을 페이지별로 조회하기 */
    public Page<HealthInfo> findByUsingPaging(final Pageable pageable) {
        return healthInfoRepository.findByUsingPaging(pageable);
    }
}
