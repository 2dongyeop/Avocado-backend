package io.wisoft.capstonedesign.domain.staff.application;


import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPhotoPathRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffHospitalRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Staff staff = findById(staffId);
        validateStaffPassword(staff, request);

        staff.updatePassword(encryptHelper.encrypt(request.getNewPassword()));
    }

    private void validateStaffPassword(final Staff staff, final UpdateStaffPasswordRequest request) {

        if (!encryptHelper.isMatch(request.getOldPassword(), staff.getPassword())) {
            throw new IllegalValueException("의료진 비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }

    /**
     * 의료진 프로필사진 수정 및 업로드
     */
    @Transactional
    public void uploadPhotoPath(final Long staffId, final UpdateStaffPhotoPathRequest request) {

        Staff staff = findById(staffId);
        staff.updatePhotoPath(request.getPhotoPath());
    }


    /* 의료진 병원 업로드 */
    @Transactional
    public void updateStaffHospital(final Long staffId, final UpdateStaffHospitalRequest request) {

        Staff staff = findById(staffId);
        Hospital hospital = hospitalService.findByHospitalName(request.getHospitalName());

        staff.updateHospital(hospital);
    }

    /* 의료진 탈퇴 */
    @Transactional
    public void deleteStaff(final Long staffId) {
        Staff staff = findById(staffId);
        staffRepository.delete(staff);
    }

    /** 상세 조회 */
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