package io.wisoft.capstonedesign.domain.appointment.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.web.bind.annotation.*;


@Tag(name = "예약")
@RestController
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @SwaggerApi(summary = "예약 저장", implementation = CreateAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/appointments")
    public CreateAppointmentResponse createAppointment(
            @RequestBody @Valid final CreateAppointmentRequest request) {
        return new CreateAppointmentResponse(appointmentService.save(request));
    }


    @SwaggerApi(summary = "예약 삭제", implementation = DeleteAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/api/appointments/{id}")
    public DeleteAppointmentResponse deleteAppointment(@PathVariable("id") final Long id) {
        appointmentService.deleteAppointment(id);
        return new DeleteAppointmentResponse(id);
    }


    @SwaggerApi(summary = "예약 수정", implementation = UpdateAppointmentResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/api/appointments/{id}")
    public UpdateAppointmentResponse updateAppointment(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateAppointmentRequest request) {

        appointmentService.update(id, request);
        return new UpdateAppointmentResponse(id);
    }

    @SwaggerApi(summary = "예약 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/appointments/{id}/details")
    public Result appointment(@PathVariable("id") final Long id) {
        return new Result(new AppointmentDto(appointmentService.findDetailById(id)));
    }
}
