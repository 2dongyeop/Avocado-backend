package io.wisoft.capstonedesign.domain.staff.application;


import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final HospitalService hospitalService;
    private final EncryptHelper encryptHelper;

    /**
     * 의료진 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long staffId, final UpdateStaffPasswordRequest request) {

        final Staff staff = findById(staffId);
        validateStaffPassword(staff, request);

        staff.updatePassword(encryptHelper.encrypt(request.newPassword()));
    }

    private void validateStaffPassword(final Staff staff, final UpdateStaffPasswordRequest request) {

        if (!encryptHelper.isMatch(request.oldPassword(), staff.getPassword())) {
            throw new IllegalValueException("의료진 비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /**
     * 의료진 정보 수정 - 프로필사진 or 병원
     */
    @Transactional
    public void updateStaff(final Long staffId, final String hospitalName, final String photoPath) {

        final Staff staff = findById(staffId);

        if (StringUtils.hasText(hospitalName)) {
            staff.updateHospital(hospitalService.findByHospitalName(hospitalName));
        }

        if (StringUtils.hasText(photoPath)) {
            staff.updatePhotoPath(photoPath);
        }
    }


    /* 의료진 탈퇴 */
    @Transactional
    public void deleteStaff(final Long staffId) {
        staffRepository.delete(findById(staffId));
    }

    /**
     * 상세 조회
     */
    public Staff findDetailById(final Long staffId) {
        return staffRepository.findDetailById(staffId).orElseThrow(NullStaffException::new);
    }

    public Staff findById(final Long staffId) {
        return staffRepository.findById(staffId).orElseThrow(NullStaffException::new);
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public List<Staff> findAllByHospital() {
        return staffRepository.findAllByHospital();
    }
}