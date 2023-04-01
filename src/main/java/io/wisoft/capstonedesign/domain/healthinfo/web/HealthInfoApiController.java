package io.wisoft.capstonedesign.domain.healthinfo.web;

import io.wisoft.capstonedesign.domain.healthinfo.application.HealthInfoService;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.*;
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


    /* 건강 정보 등록 */
    @PostMapping("/api/health-infos/new")
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        Long id = healthInfoService.save(request);
        HealthInfo healthInfo = healthInfoService.findById(id);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    /* 건강 정보 삭제 */
    @DeleteMapping("/api/health-infos/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {
        HealthInfo healthInfo = healthInfoService.findById(id);
        healthInfo.delete();

        return new DeleteHealthInfoResponse(id);
    }


    /* 건강 정보 단건 조회 */
    @GetMapping("/api/health-infos/{id}")
    public Result healthInfo(@PathVariable("id") final Long id) {
        HealthInfo healthInfo = healthInfoService.findById(id);

        return new Result(new HealthInfoDto(healthInfo));
    }


    /* 건강 정보 목록 조회 */
    @GetMapping("/api/health-infos")
    public Result healthInfos() {
        List<HealthInfoDto> infoDtoList = healthInfoService.findAll()
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());

        return new Result(infoDtoList);
    }


    /* 특정 병과의 건강 정보 목록 조회 */
    @GetMapping("/api/health-infos/department")
    public Result healthInfosByDepartment(@RequestBody @Valid final HealthInfoByDepartmentRequest request) {

        List<HealthInfoDto> infoDtoList = healthInfoService.findAllByDept(request.getDepartment())
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
}
