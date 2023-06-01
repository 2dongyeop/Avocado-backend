package io.wisoft.capstonedesign.domain.hospital.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
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

        validateDuplicateHospital(request);

        final Hospital hospital = createHospital(request);

        hospitalRepository.save(hospital);
        return hospital.getId();
    }

    private Hospital createHospital(final CreateHospitalRequest request) {
        return Hospital.builder()
                .name(request.name())
                .number(request.number())
                .address(request.address())
                .operatingTime(request.operatingTime())
                .build();
    }

    private void validateDuplicateHospital(final CreateHospitalRequest request) {
        final List<Hospital> hospitalList = hospitalRepository.findByName(request.name());
        if (!hospitalList.isEmpty()) throw new DuplicateHospitalException();
    }

    /**
     * 병원 단건 조회
     */
    public Hospital findById(final Long hospitalId) {
        return hospitalRepository.findById(hospitalId).orElseThrow(NullHospitalException::new);
    }

    /* 병원 이름으로 조회 */
    public Hospital findByHospitalName(final String hospitalName) {
        final List<Hospital> hospitalList = hospitalRepository.findByName(hospitalName);

        if (hospitalList.isEmpty()) {
            throw new NullHospitalException();
        }
        return hospitalList.get(0);
    }

    /**
     * 병원 목록 조회
     */
    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }
}
