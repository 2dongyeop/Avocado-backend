package io.wisoft.capstonedesign.domain.staff.application;


import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.domain.staff.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPhotoPathRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffHospitalRequest;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
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

    /**
     * 의료진가입
     */
    @Transactional
    public Long signUp(final CreateStaffRequest request) {

        //엔티티 조회
        Hospital hospital = hospitalService.findOne(request.getHospitalId());

        Staff staff = Staff.builder()
                .hospital(hospital)
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .license_path(request.getLicensePath())
                .dept(HospitalDept.valueOf(request.getDept()))
                .build();

        staffRepository.signUp(staff);
        return staff.getId();
    }

    /**
     * 의료진 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long staffId, final UpdateStaffPasswordRequest request) {

        Staff staff = findOne(staffId);
        validateStaffPassword(staff, request);

        staff.updatePassword(request.getNewPassword());
    }

    private void validateStaffPassword(final Staff staff, final UpdateStaffPasswordRequest request) {

        if (!staff.getPassword().equals(request.getOldPassword())) {
            throw new IllegalValueException("의료진 비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }

    /**
     * 의료진 프로필사진 수정 및 업로드
     */
    @Transactional
    public void uploadPhotoPath(final Long staffId, final UpdateStaffPhotoPathRequest request) {

        Staff staff = findOne(staffId);
        staff.updatePhotoPath(request.getPhotoPath());
    }


    /* 의료진 병원 업로드 */
    @Transactional
    public void updateStaffHospital(final Long staffId, final UpdateStaffHospitalRequest request) {

        Staff staff = findOne(staffId);
        Hospital hospital = hospitalService.findByHospitalName(request.getHospitalName());

        staff.updateHospital(hospital);
    }

    /* 의료진 탈퇴 */
    @Transactional
    public void deleteStaff(final Long staffId) {
        staffRepository.delete(staffId);
    }


    /**
     * 자신이 속한 병원의 리뷰 목록 조회
     */
    public List<Review> findReviewByStaffHospitalName(final Long staffId) {

        Staff staff = findOne(staffId);
        String hospitalName = staff.getHospital().getName();

        return staffRepository.findReviewListByStaffHospitalName(hospitalName);
    }

    /**
     * 자신이 댓글 단 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(final Long staffId) {
        return staffRepository.findBoardListByStaffId(staffId);
    }


    /**
     * 의료진 조회
     */
    public Staff findOne(final Long staffId) {
        return staffRepository.findOne(staffId).orElseThrow(NullStaffException::new);
    }

    public List<Staff> findAll() { return staffRepository.findAll(); }

    public List<Staff> findAllByHospital() {
        return staffRepository.findAllByHospital();
    }
}