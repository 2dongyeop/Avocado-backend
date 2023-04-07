package io.wisoft.capstonedesign.domain.staff.application;


import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.web.dto.LoginRequest;
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
import io.wisoft.capstonedesign.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final HospitalService hospitalService;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 의료진가입
     */
    @Transactional
    public Long signUp(final CreateStaffRequest request) {

        validateDuplicateStaff(request);

        //엔티티 조회
        Hospital hospital = hospitalService.findById(request.getHospitalId());

        Staff staff = Staff.builder()
                .hospital(hospital)
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptHelper.encrypt(request.getPassword1()))
                .license_path(request.getLicensePath())
                .dept(HospitalDept.valueOf(request.getDept()))
                .build();

        staffRepository.save(staff);
        return staff.getId();
    }

    private void validateDuplicateStaff(final CreateStaffRequest request) {
        List<Staff> staffList = staffRepository.findValidateByEmail(request.getEmail());
        if (!staffList.isEmpty()) throw new DuplicateStaffException();
    }


    /**
     * 의료진 로그인
     */
    public String login(final LoginRequest request) {

        Staff staff = staffRepository.findByEmail(request.getEmail())
                .orElseThrow(NullStaffException::new);

        if (!encryptHelper.isMatch(request.getPassword(), staff.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(staff.getName());
    }


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


    /**
     * 자신이 속한 병원의 리뷰 목록 조회
     */
    public List<Review> findReviewByStaffHospitalName(final Long staffId) {

        Staff staff = findById(staffId);
        String hospitalName = staff.getHospital().getName();

        return staffRepository.findReviewListByStaffHospitalName(hospitalName);
    }

    /**
     * 자신이 댓글 단 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(final Long staffId) {
        List<BoardReply> boardReplyListByStaffId = staffRepository.findBoardReplyListByStaffId(staffId);

        List<Board> boardList = boardReplyListByStaffId.stream()
                .map(BoardReply::getBoard)
                .collect(Collectors.toList());

        return boardList;
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