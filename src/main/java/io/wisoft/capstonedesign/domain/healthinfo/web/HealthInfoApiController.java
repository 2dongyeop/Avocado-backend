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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "건강정보")
@Slf4j
@RestController
@RequestMapping("/api/health-infos")
@RequiredArgsConstructor
public class HealthInfoApiController {

    private final HealthInfoService healthInfoService;


    @SwaggerApi(summary = "건강 정보 등록", implementation = CreateHealthInfoResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        log.info("CreateHealthInfoRequest[{}]", request);

        final Long id = healthInfoService.save(request);
        final HealthInfo healthInfo = healthInfoService.findById(id);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    @SwaggerApi(summary = "건강 정보 삭제", implementation = DeleteHealthInfoResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {

        log.info("HealthInfo Id[{}]", id);

        healthInfoService.delete(id);
        return new DeleteHealthInfoResponse(id);
    }


    @SwaggerApi(summary = "건강 정보 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result healthInfo(@PathVariable("id") final Long id) {

        log.info("HealthInfo Id[{}]", id);
        return new Result(new HealthInfoDto(healthInfoService.findDetailById(id)));
    }


    /**
     * ex) /api/health-infos?dept=1&dept=2&page=0&size=5&sort=createAt,desc -> 다중 dept 조회
     * ex) /api/health-infos?dept=1&page=0&size=5&sort=createAt,desc        -> dept 하나 조회
     * ex) /api/health-infos?page=0&size=5&sort=createAt,desc               -> dept 없으면 모든 건강정보 조회
     */
    @SwaggerApi(summary = "특정 병과의 건강 정보 목록 페이징 조회", implementation = Page.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping
    public Page<HealthInfoDto> healthInfosByDepartmentUsingPaging(
            @RequestParam(required = false) final MultiValueMap<String, String> paramMap,
            final Pageable pageable) {

        final List<String> deptNumberList = paramMap.get("dept");

        if (deptNumberList.isEmpty()) {
            return healthInfoService.findByUsingPaging(pageable).map(HealthInfoDto::new);
        }

        return healthInfoService.findAllByDeptUsingPagingMultiValue(deptNumberList, pageable).map(HealthInfoDto::new);
    }
}
