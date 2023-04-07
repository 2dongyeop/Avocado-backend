package io.wisoft.capstonedesign.domain.staff.web;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.web.dto.BoardDto;
import io.wisoft.capstonedesign.domain.member.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.member.web.dto.TokenResponse;
import io.wisoft.capstonedesign.domain.review.web.dto.ReviewDto;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.web.dto.*;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StaffApiController {

    private final StaffService staffService;

    /* 의료진 단건 상세 조회 */
    @GetMapping("api/staff/{id}")
    public Result staff(@PathVariable("id") final Long id) {

        Staff staff = staffService.findById(id);

        return new Result(new StaffDto(staff));
    }

    /* 의료진 목록 조회 */
    @GetMapping("/api/staff")
    public Result staffs() {

        List<StaffDto> staffDtoList = staffService.findAllByHospital().stream()
                .map(StaffDto::new)
                .collect(Collectors.toList());

        return new Result(staffDtoList);
    }


    /* 자신이 댓글을 작성한 게시글 목록 조회 */
    @GetMapping("/api/staff/{staff-id}/boards")
    public Result boardListByStaffId(@PathVariable("staff-id") final Long id) {

        List<BoardDto> boardDtoList = staffService.findBoardListByStaffId(id).stream()
                .map(BoardDto::new)
                .collect(Collectors.toList());

        return new Result(boardDtoList);
    }


    /* 자신이 속한 병원의 리뷰 목록 조회 */
    @GetMapping("/api/staff/{staff-id}/reviews")
    public Result reviewListByStaffId(@PathVariable("staff-id") final Long id) {

        List<ReviewDto> reviewDtoList = staffService.findReviewByStaffHospitalName(id)
                .stream().map(ReviewDto::new)
                .collect(Collectors.toList());

        return new Result(reviewDtoList);
    }


    /* 의료진 가입 */
    @PostMapping("/api/staff/signup")
    public CreateStaffResponse saveStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        if (!request.getPassword1().equals(request.getPassword2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }

        Long id = staffService.signUp(request);
        return new CreateStaffResponse(id);
    }


    /** 의료진 로그인 */
    @PostMapping("/api/staff/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid final LoginRequest request) {

        String token = staffService.login(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }


    /* 의료진 비밀번호 수정 */
    @PatchMapping("/api/staff/{id}/password")
    public UpdateStaffResponse updateStaffPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPasswordRequest request) {

        staffService.updatePassword(id, request);
        Staff staff = staffService.findById(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 프로필사진 업로드 */
    @PatchMapping("/api/staff/{id}/photo")
    public UpdateStaffResponse uploadStaffPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPhotoPathRequest request) {

        staffService.uploadPhotoPath(id, request);
        Staff staff = staffService.findById(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 병원 변경 */
    @PatchMapping("/api/staff/{id}/hospital")
    public UpdateStaffResponse updateStaffHospital(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffHospitalRequest request) {

        staffService.updateStaffHospital(id, request);
        Staff staff = staffService.findById(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 탈퇴 */
    @DeleteMapping("api/staff/{id}")
    public DeleteStaffResponse deleteStaff(@PathVariable("id") final Long id) {

        staffService.deleteStaff(id);
        return new DeleteStaffResponse(id);
    }
}
