package io.wisoft.capstonedesign.domain.appointment.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Tag(name = "예약")
@Slf4j
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @SwaggerApi(summary = "예약 저장", implementation = CreateAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateAppointmentResponse createAppointment(
            @RequestBody @Valid final CreateAppointmentRequest request) {
        log.info("CreateAppointmentRequest[{}]", request);
        return new CreateAppointmentResponse(appointmentService.save(request));
    }


    @SwaggerApi(summary = "예약 삭제", implementation = DeleteAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteAppointmentResponse deleteAppointment(@PathVariable("id") final Long id) {
        log.info("appointment Id[{}]", id);
        appointmentService.deleteAppointment(id);
        return new DeleteAppointmentResponse(id);
    }


    @SwaggerApi(summary = "예약 수정", implementation = UpdateAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateAppointmentResponse updateAppointment(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateAppointmentRequest request) {

        log.info("appointment Id[{}], UpdateAppointmentRequest[{}]", id, request);
        appointmentService.update(id, request);
        return new UpdateAppointmentResponse(id);
    }

    @SwaggerApi(summary = "예약 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/{id}/details")
    public Result appointment(@PathVariable("id") final Long id) {
        log.info("appointment Id[{}]", id);
        return new Result(new AppointmentDto(appointmentService.findDetailById(id)));
    }
}
