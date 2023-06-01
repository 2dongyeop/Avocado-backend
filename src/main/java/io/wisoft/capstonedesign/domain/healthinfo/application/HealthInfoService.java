package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfoRepository;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.global.mapper.DeptMapper;
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

        final Staff staff = staffService.findById(request.staffId());

        final HealthInfo healthInfo = createHealthInfo(request, staff);

        healthInfoRepository.save(healthInfo);
        return healthInfo.getId();
    }

    private HealthInfo createHealthInfo(final CreateHealthInfoRequest request, final Staff staff) {
        return HealthInfo.builder()
                .staff(staff)
                .healthInfoPath(request.healthInfoPath())
                .title(request.title())
                .dept(HospitalDept.valueOf(request.dept()))
                .build();
    }

    /**
     * 건강정보 삭제
     */
    @Transactional
    public void delete(final Long healthInfoId) {
        healthInfoRepository.delete(findById(healthInfoId));
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


    /**
     * 특정 병과의 건강정보 목록을 페이지별로 조회하기
     */
    public Page<HealthInfo> findAllByDeptUsingPaging(final String deptNumber, final Pageable pageable) {

        return healthInfoRepository.findAllByDeptUsingPaging(DeptMapper.numberToDept(deptNumber), pageable);
    }

    /**
     * 건강정보 목록을 페이지별로 조회하기
     */
    public Page<HealthInfo> findByUsingPaging(final Pageable pageable) {
        return healthInfoRepository.findByUsingPaging(pageable);
    }
}
