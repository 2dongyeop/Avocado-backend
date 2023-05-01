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
    @PostMapping("/api/health-infos/new")
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
     * ex) /api/health-infos/department?page=0&size=5&sort=createAt,desc
     */
    @SwaggerApi(summary = "특정 병과의 건강 정보 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/health-infos/department")
    public Page<HealthInfoDto> healthInfosByDepartmentUsingPaging(
            @RequestBody @Valid final HealthInfoByDepartmentRequest request, final Pageable pageable) {

        return healthInfoService.findAllByDeptUsingPaging(request.department(), pageable).map(HealthInfoDto::new);
    }


    /**
     * ex) /api/health-infos?page=0&size=5&sort=createAt,desc
     */
    @SwaggerApi(summary = "건강정보 목록을 페이지별로 조회하기", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/health-infos")
    public Page<HealthInfoDto> healthInfosUsingPaging(final Pageable pageable) {
        return healthInfoService.findByUsingPaging(pageable).map(HealthInfoDto::new);
    }
}
