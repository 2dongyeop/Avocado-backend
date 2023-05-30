package io.wisoft.capstonedesign.domain.healthinfo.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.healthinfo.application.HealthInfoService;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Tag(name = "건강정보")
@RestController
@RequiredArgsConstructor
public class HealthInfoApiController {

    private final HealthInfoService healthInfoService;


    @SwaggerApi(summary = "건강 정보 등록", implementation = CreateHealthInfoResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/health-infos")
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        final Long id = healthInfoService.save(request);
        final HealthInfo healthInfo = healthInfoService.findById(id);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    @SwaggerApi(summary = "건강 정보 삭제", implementation = DeleteHealthInfoResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/api/health-infos/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {
        healthInfoService.delete(id);
        return new DeleteHealthInfoResponse(id);
    }


    @SwaggerApi(summary = "건강 정보 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/health-infos/{id}/details")
    public Result healthInfo(@PathVariable("id") final Long id) {
        return new Result(new HealthInfoDto(healthInfoService.findDetailById(id)));
    }


    /**
     * ex) /api/health-infos?dept=1&page=0&size=5&sort=createAt,desc
     * ex) /api/health-infos?page=0&size=5&sort=createAt,desc
     */
    @SwaggerApi(summary = "특정 병과의 건강 정보 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/health-infos")
    public Page<HealthInfoDto> healthInfosByDepartmentUsingPaging(
            @RequestParam(name = "dept", required = false) final String deptNumber, final Pageable pageable) {

        if (deptNumber == null) {
            return healthInfoService.findByUsingPaging(pageable).map(HealthInfoDto::new);
        }

        return healthInfoService.findAllByDeptUsingPaging(deptNumber, pageable).map(HealthInfoDto::new);
    }
}
