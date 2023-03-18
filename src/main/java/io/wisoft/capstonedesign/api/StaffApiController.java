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

    /* 의료진 조회 */
    @GetMapping("/api/staffs")
    public Result staffs() {

        List<Staff> staffList = staffService.findAllByHospital();

        List<StaffDto> staffDtoList = staffList.stream()
                .map(StaffDto::new)
                .collect(Collectors.toList());

        return new Result(staffDtoList);
    }


    /* 의료진 가입 */
    @PostMapping("/api/staffs")
    public CreateStaffResponse saveStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        Long id = staffService.signUp(request.hospitalId, request.name, request.email, request.password, request.licensePath, HospitalDept.valueOf(request.dept));

        return new CreateStaffResponse(id);
    }

    /* 의료진 비밀번호 수정 */
    @PatchMapping("/api/staffs/{id}")
    public UpdateStaffResponse updateStaffPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPasswordRequest request) {

        staffService.updatePassword(id, request.oldPassword, request.newPassword);
        Staff staff = staffService.findOne(id);

        return new UpdateStaffResponse(staff.getId());
    }


    /* 의료진 프로필사진 업로드 */
    @PostMapping("/api/staffs/{id}")
    public UpdateStaffResponse uploadStaffPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPhotoPathRequest request) {

        staffService.uploadPhotoPath(id, request.photoPath);
        Staff staff = staffService.findOne(id);

        return new UpdateStaffResponse(staff.getId());
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
