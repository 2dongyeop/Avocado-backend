package io.wisoft.capstonedesign.healthinfo;

import io.wisoft.capstonedesign.staff.Staff;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.staff.StaffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HealthInfoApiController {

    private final HealthInfoService healthInfoService;
    private final StaffService staffService;


    /* 건강 정보 등록 */
    @PostMapping("/api/health-infos/new")
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        Staff staff = staffService.findOne(request.staffId);
        HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, request.healthInfoPath, request.title, HospitalDept.valueOf(request.dept));

        healthInfoService.save(healthInfo);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    /* 건강 정보 삭제 */
    @DeleteMapping("/api/health-infos/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {
        HealthInfo healthInfo = healthInfoService.findOne(id);
        healthInfo.delete();

        return new DeleteHealthInfoResponse(id);
    }


    /* 건강 정보 단건 조회 */
    @GetMapping("/api/health-infos/{id}")
    public Result healthInfo(@PathVariable("id") final Long id) {
        HealthInfo healthInfo = healthInfoService.findOne(id);

        return new Result(new HealthInfoDto(healthInfo));
    }


    /* 건강 정보 목록 조회 */
    @GetMapping("/api/health-infos")
    public List<HealthInfoDto> healthInfos() {
        List<HealthInfoDto> infoDtoList = healthInfoService.findAll()
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());

        return infoDtoList;
//        return new Result(infoDtoList);
    }


    /* 특정 병과의 건강 정보 목록 조회 */
    @GetMapping("/api/health-infos/department")
    public Result healthInfosByDepartment(@RequestBody @Valid final HealthInfoByDepartmentRequest request) {

        List<HealthInfoDto> infoDtoList = healthInfoService.findAllByDept(request.department)
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());

        return new Result(infoDtoList);
    }


    /* 건강 정보 조회 - 작성 시간을 기준으로 오름차순 */
    @GetMapping("/api/health-infos/create-asc")
    public Result healthInfosOrderByCreateAsc() {
        List<HealthInfoDto> infoDtoList = healthInfoService.findAllOrderByCreateAsc()
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());

        return new Result(infoDtoList);
    }


    /* 건강 정보 조회 - 작성 시간을 기준으로 내림차순 */
    @GetMapping("/api/health-infos/create-desc")
    public Result healthInfosOrderByCreateDesc() {
        List<HealthInfoDto> infoDtoList = healthInfoService.findAllOrderByCreateDesc()
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());

        return new Result(infoDtoList);
    }


    @Data
    @AllArgsConstructor
    static class HealthInfoDto {
        private String writer;
        private String title;
        private String dept;
        private String healthInfoPath;

        public HealthInfoDto(final HealthInfo healthInfo) {
            this.writer = healthInfo.getStaff().getName();
            this.title = healthInfo.getTitle();
            this.dept = healthInfo.getDept().toString();
            this.healthInfoPath = healthInfo.getHealthInfoPath();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class DeleteHealthInfoResponse {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    static class CreateHealthInfoResponse {
        private Long id;
    }

    @Data
    static class HealthInfoByDepartmentRequest {
        private String department;
    }

    @Data
    static class CreateHealthInfoRequest {
        private Long staffId;
        private String title;
        private String dept;
        private String healthInfoPath;
    }
}
