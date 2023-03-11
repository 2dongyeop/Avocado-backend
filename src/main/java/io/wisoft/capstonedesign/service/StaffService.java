package io.wisoft.capstonedesign.service;


import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.repository.StaffRepository;
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

    /**
     * 회원가입
     */
    @Transactional
    public Long signUp(Long hospitalId, String name, String email, String password, String license_path, HospitalDept dept) {

        //엔티티 조회
        Hospital hospital = hospitalService.findOne(hospitalId);

        Staff staff = Staff.newInstance(hospital, name, email, password, license_path, dept);

        validateDuplicateStaff(staff);
        staffRepository.signUp(staff);
        return staff.getId();
    }

    @Transactional
    public Long signUp(Long hospitalId, String name, String email, String password, String license_path, HospitalDept dept, String staffPhotoPath) {

        //엔티티 조회
        Hospital hospital = hospitalService.findOne(hospitalId);

        Staff staff = Staff.newInstance(hospital, name, email, password, license_path, dept, staffPhotoPath);

        validateDuplicateStaff(staff);
        staffRepository.signUp(staff);
        return staff.getId();
    }

    private void validateDuplicateStaff(Staff staff) {
        List<Staff> findStaffsByEmail = staffRepository.findByEmail(staff.getEmail());
        if (findStaffsByEmail.size() != 0) {
            throw new DuplicateStaffException("중복 의료진 발생 : 이미 존재하는 의료진입니다.");
        }
    }

    /**
     * 의료진 조회
     */
    public Staff findOne(Long staffId) { return staffRepository.findOne(staffId); }

    public List<Staff> findAll() { return staffRepository.findAll(); }
}