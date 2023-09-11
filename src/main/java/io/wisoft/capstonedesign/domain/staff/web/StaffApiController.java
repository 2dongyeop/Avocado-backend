package io.wisoft.capstonedesign.domain.staff.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.domain.staff.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "의료진")
@Slf4j
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffApiController {

    private final StaffService staffService;

    @SwaggerApi(summary = "의료진 단건 상세 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/{id}/details")
    public Result staff(@PathVariable("id") final Long id) {

        log.debug("Staff Id[{}]", id);
        return new Result(new StaffDto(staffService.findDetailById(id)));
    }

    @SwaggerApi(summary = "의료진 목록 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping
    public Result staffs() {

        return new Result(staffService.findAllByHospital().stream()
                .map(StaffDto::new)
                .collect(Collectors.toList()));
    }


    @SwaggerApi(summary = "의료진 비밀번호 변경", implementation = UpdateStaffResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}/password")
    public UpdateStaffResponse updateStaffPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateStaffPasswordRequest request) {

        log.debug("Staff Id[{}], UpdateStaffPasswordRequest[{}]", id, request);

        staffService.updatePassword(id, request);
        final Staff staff = staffService.findById(id);

        return new UpdateStaffResponse(staff.getId());
    }


    @SwaggerApi(summary = "의료진 정보 수정", implementation = UpdateStaffResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateStaffResponse updateStaff(
            @PathVariable("id") final Long id,
            @RequestParam(value = "hospitalName", required = false) final String hospitalName,
            @RequestParam(value = "photoPath", required = false) final String photoPath) {

        log.debug("hospitalName[{}], photoPath[{}]", hospitalName, photoPath);

        staffService.updateStaff(id, hospitalName, photoPath);
        final Staff staff = staffService.findById(id);

        return new UpdateStaffResponse(staff.getId());
    }


    @SwaggerApi(summary = "의료진 탈퇴", implementation = DeleteStaffResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteStaffResponse deleteStaff(@PathVariable("id") final Long id) {

        log.debug("Staff Id[{}]", id);

        staffService.deleteStaff(id);
        return new DeleteStaffResponse(id);
    }
}
