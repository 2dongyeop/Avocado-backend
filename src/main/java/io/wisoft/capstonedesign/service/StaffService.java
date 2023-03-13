package io.wisoft.capstonedesign.service;


import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Review;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.IllegalValueException;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.exception.nullcheck.NullStaffException;
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
     * 의료진 비밀번호 수정
     */
    public void updatePassword(Long staffId, String oldPassword, String newPassword) {

        Staff staff = findOne(staffId);
        validateStaffPassword(staff, oldPassword);

        staff.updatePassword(newPassword);
    }

    private void validateStaffPassword(Staff staff, String oldPassword) {

        if (!staff.getPassword().equals(oldPassword)) {
            throw new IllegalValueException("의료진 비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }

    /**
     * 의료진 프로필사진 수정
     */
    public void updatePhotoPath(Long staffId, String newPhotoPath) {

        Staff staff = findOne(staffId);
        staff.updatePhotoPath(newPhotoPath);
    }


    /**
     * 자신이 속한 병원의 리뷰 목록 조회
     */
    public List<Review> findReviewByStaffHospitalName(Long staffId) {

        Staff staff = staffRepository.findOne(staffId);
        String hospitalName = staff.getHospital().getName();

        return staffRepository.findReviewListByStaffHospitalName(hospitalName);
    }

    /**
     * 자신이 댓글 단 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(Long staffId) {
        return staffRepository.findBoardListByStaffId(staffId);
    }


    /**
     * 의료진 조회
     */
    public Staff findOne(Long staffId) {

        Staff getStaff = staffRepository.findOne(staffId);

        if (getStaff == null) {
            throw new NullStaffException("해당 의료진 정보가 존재하지 않습니다.");
        }
        return getStaff;
    }

    public List<Staff> findAll() { return staffRepository.findAll(); }
}