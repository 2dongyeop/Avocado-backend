package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.HealthInfo;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.repository.HealthInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HealthInfoService {

    private final HealthInfoRepository healthInfoRepository;

    /**
     * 건강정보 저장
     */
    @Transactional
    public Long save(HealthInfo healthInfo) {

        healthInfoRepository.save(healthInfo);
        return healthInfo.getId();
    }

    /**
     * 건강정보 삭제
     */
    @Transactional
    public void delete(Long healthInfoId) {

        HealthInfo healthInfo = healthInfoRepository.findOne(healthInfoId);
        healthInfo.delete();
    }

    /* 조회 로직 */
    public HealthInfo findOne(Long healthInfoId) {
        HealthInfo getHealthInfo = healthInfoRepository.findOne(healthInfoId);

        if (getHealthInfo == null) {
            throw new NullHealthInfoException("해당 건강정보가 조회하지 않습니다.");
        }

        return getHealthInfo;
    }

    public List<HealthInfo> findAll() {
        return healthInfoRepository.findAll();
    }
}
