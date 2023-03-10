package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.repository.HospitalRepository;
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
    public Long save(String name, String number, String address, String operatingTime) {

        Hospital hospital = Hospital.createHospital(name, number, address, operatingTime);

        hospitalRepository.save(hospital);
        return hospital.getId();
    }

    /**
     * 병원 단건 조회
     */
    public Hospital findOne(Long hospitalId) {
        Hospital getHospital = hospitalRepository.findOne(hospitalId);

        if (getHospital == null) {
            throw new NullHospitalException("해당 병원은 존재하지 않습니다.");
        }
        return getHospital;
    }

    /**
     * 병원 목록 조회
     */
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }
}
