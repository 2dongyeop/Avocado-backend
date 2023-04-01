package io.wisoft.capstonedesign.domain.appointment.web;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.web.dto.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    /* 예약 저장 */
    @PostMapping("/api/appointments/new")
    public CreateAppointmentResponse createAppointment(
            @RequestBody @Valid final CreateAppointmentRequest request) {

        Long id = appointmentService.save(request);
        return new CreateAppointmentResponse(id);
    }


    /* 예약 삭제 */
    @DeleteMapping("/api/appointments/{id}")
    public DeleteAppointmentResponse deleteAppointment(@PathVariable("id") final Long id) {

        appointmentService.cancelAppointment(id);
        Appointment appointment = appointmentService.findById(id);
        return new DeleteAppointmentResponse(appointment.getId(), appointment.getStatus().toString());
    }


    /* 예약 정보 수정 */
    @PatchMapping("/api/appointments/{id}")
    public UpdateAppointmentResponse updateAppointment(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateAppointmentRequest request) {

        appointmentService.update(id, request);
        return new UpdateAppointmentResponse(id);
    }

    /* 예약 정보 단건 조회 */
    @GetMapping("/api/appointments/{id}")
    public Result appointment(@PathVariable("id") final Long id) {

        Appointment appointment = appointmentService.findById(id);
        return new Result(new AppointmentDto(appointment));
    }


    /* 특정 회원의 예약 정보 목록 조회 */
    @GetMapping("/api/appointments/member/{member-id}")
    public Result appointmentsByMember(@PathVariable("member-id") final Long id) {
        List<AppointmentDto> appointmentDtoList = appointmentService.findByMemberId(id)
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());

        return new Result(appointmentDtoList);
    }


    /* 특정 회원의 예약 정보 목록 오름차순 조회 */
    @GetMapping("/api/appointments/member/{member-id}/create-asc")
    public Result appointmentsByMemberOrderByCreateAsc(@PathVariable("member-id") final Long id) {
        List<AppointmentDto> appointmentDtoList = appointmentService.findByMemberIdOrderByCreateAtAsc(id)
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());

        return new Result(appointmentDtoList);
    }


    /* 특정 회원의 예약 정보 목록 내림차순 조회 */
    @GetMapping("/api/appointments/member/{member-id}/create-desc")
    public Result appointmentsByMemberOrderByCreateDesc(@PathVariable("member-id") final Long id) {
        List<AppointmentDto> appointmentDtoList = appointmentService.findByMemberIdOrderByCreateAtDesc(id)
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());

        return new Result(appointmentDtoList);
    }
}
