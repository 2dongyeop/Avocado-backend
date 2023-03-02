package io.wisoft.capstonedesign.service;


import io.wisoft.capstonedesign.domain.Staff;
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

    /**
     * 회원가입
     */
    @Transactional
    public Long signUp(Staff staff) {

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