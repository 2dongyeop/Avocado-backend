package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.service.StaffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

        Staff staff = staffService.findOne(id);

        return new Result(new StaffDto(staff.getName(), staff.getEmail(), staff.getDept().toString(), staff.getHospital().toString()));
    }

    /* 의료진 목록 조회 */
    @GetMapping("/api/staff")
    public Result staffs() {

        List<StaffDto> staffDtoList = staffService.findAllByHospital().stream()
                .map(StaffDto::new)
                .collect(Collectors.toList());

        return new Result(staffDtoList);
    }


    /* 의료진 가입 */
    @PostMapping("/api/staff/signup")
    public CreateStaffResponse saveStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        Long id = staffService.signUp(request.hospitalId, request.name, request.email, request.password, request.licensePath, HospitalDept.valueOf(request.dept));

        return new CreateStaffResponse(id);
    }

    /* 의료진 비밀번호 수정 */
    @PatchMapping("/api/staff/{id}/password")
    public UpdateStaffResponse updateStaffPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPasswordRequest request) {

        staffService.updatePassword(id, request.oldPassword, request.newPassword);
        Staff staff = staffService.findOne(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 프로필사진 업로드 */
    @PatchMapping("/api/staff/{id}/photo")
    public UpdateStaffResponse uploadStaffPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPhotoPathRequest request) {

        staffService.uploadPhotoPath(id, request.photoPath);
        Staff staff = staffService.findOne(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 병원 변경 */
    @PatchMapping("/api/staff/{id}/hospital")
    public UpdateStaffResponse updateStaffHospital(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffRequest request) {

        staffService.updateStaffHospital(id, request.hospitalName);
        Staff staff = staffService.findOne(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 탈퇴 */
    @DeleteMapping("api/staff/{id}")
    public DeleteStaffResponse deleteStaff(@PathVariable("id") final Long id) {

        staffService.deleteStaff(id);
        return new DeleteStaffResponse(id);
    }


    @Data
    @AllArgsConstructor
    static class DeleteStaffResponse {
        private Long id;
    }

    @Data
    static class UpdateStaffRequest {
        private String hospitalName;
    }

    @Data
    @AllArgsConstructor
    static class StaffDto {
        private String name;
        private String email;
        private String dept;
        private String hospital;

        public StaffDto(Staff staff) {
            this.name = staff.getName();
            this.email = staff.getEmail();
            this.dept = String.valueOf(staff.getDept());
            this.hospital = staff.getHospital().getName();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class UpdateStaffPhotoPathRequest {
        private String photoPath;
    }

    @Data
    @AllArgsConstructor
    static class UpdateStaffResponse {
        private Long id;
    }

    @Data
    static class UpdateStaffPasswordRequest {
        private String oldPassword;
        private String newPassword;
    }


    @Data
    @AllArgsConstructor
    static class CreateStaffResponse {
        private Long id;
    }

    @Data
    static class CreateStaffRequest {
        private Long hospitalId;
        private String name;
        private String email;
        private String password;
        private String licensePath;
        private String dept;
    }
}
