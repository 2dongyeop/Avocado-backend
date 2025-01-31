package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfoRepository;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        log.info("staff[{}]", staff);

        final HealthInfo healthInfo = createHealthInfo(request, staff);
        log.info("healthInfo[{}]", healthInfo);

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
        return healthInfoRepository.findById(healthInfoId).orElseThrow(() -> {
            log.info("healthInfoId[{}] not found", healthInfoId);
            return new NotFoundException("건강정보 조회 실패");
        });
    }

    public HealthInfo findDetailById(final Long healthInfoId) {
        return healthInfoRepository.findDetailById(healthInfoId).orElseThrow(() -> {
            log.info("healthInfoId[{}] not found", healthInfoId);
            return new NotFoundException("건강정보 조회 실패");
        });
    }

    public List<HealthInfo> findAll() {
        return healthInfoRepository.findAll();
    }

    /**
     * 특정 병과의 건강정보 목록을 페이지별로 조회하기
     */
    public Page<HealthInfo> findAllByDeptUsingPagingMultiValue(final List<String> deptList, final Pageable pageable) {

        final List<HospitalDept> list = deptList.stream()
                .map(DeptMapper::numberToDept)
                .toList();

        return healthInfoRepository.findAllByDeptUsingPagingMultiValue(list, pageable);
    }

    /**
     * 건강정보 목록을 페이지별로 조회하기
     */
    public Page<HealthInfo> findByUsingPaging(final Pageable pageable) {
        return healthInfoRepository.findByUsingPaging(pageable);
    }
}
