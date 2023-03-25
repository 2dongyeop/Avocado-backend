package io.wisoft.capstonedesign.domain.hospital.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    /**
     * 병원 저장
     */
    @Transactional
    public Long save(final CreateHospitalRequest request) {

        Hospital hospital = Hospital.createHospital(
                request.getName(), request.getNumber(), request.getAddress(), request.getOperatingTime()
        );

        hospitalRepository.save(hospital);
        return hospital.getId();
    }

    /**
     * 병원 단건 조회
     */
    public Hospital findOne(final Long hospitalId) {
        Hospital getHospital = hospitalRepository.findOne(hospitalId);

        if (getHospital == null) {
            throw new NullHospitalException("해당 병원은 존재하지 않습니다.");
        }
        return getHospital;
    }

    /* 병원 이름으로 조회 */
    public Hospital findByHospitalName(final String hospitalName) {
        List<Hospital> hospitals = hospitalRepository.findByHospitalName(hospitalName);

        if (hospitals.size() == 0) {
            throw new NullHospitalException("해당 병원은 존재하지 않습니다.");
        }
        return hospitals.get(0);
    }

    /**
     * 병원 목록 조회
     */
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }
}
