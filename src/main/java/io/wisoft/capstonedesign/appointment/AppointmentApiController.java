package io.wisoft.capstonedesign.appointment;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
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

        Long id = appointmentService.save(request.memberId, request.hospitalId, HospitalDept.valueOf(request.dept), request.comment, request.appointName, request.appointPhonenumber);
        return new CreateAppointmentResponse(id);
    }


    /* 예약 삭제 */
    @DeleteMapping("/api/appointments/{id}")
    public DeleteAppointmentResponse deleteAppointment(@PathVariable("id") final Long id) {

        appointmentService.cancelAppointment(id);
        Appointment appointment = appointmentService.findOne(id);
        return new DeleteAppointmentResponse(appointment.getId(), appointment.getStatus().toString());
    }


    /* 예약 정보 수정 */
    @PatchMapping("/api/appointments/{id}")
    public UpdateAppointmentResponse updateAppointment(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateAppointmentRequest request) {

        appointmentService.update(id, HospitalDept.valueOf(request.dept), request.comment, request.appointName, request.appointPhonenumber);
        return new UpdateAppointmentResponse(id);
    }

    /* 예약 정보 단건 조회 */
    @GetMapping("/api/appointments/{id}")
    public Result appointment(@PathVariable("id") final Long id) {

        Appointment appointment = appointmentService.findOne(id);
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
        List<AppointmentDto> appointmentDtoList = appointmentService.findByMemberIdASC(id)
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());

        return new Result(appointmentDtoList);
    }


    /* 특정 회원의 예약 정보 목록 내림차순 조회 */
    @GetMapping("/api/appointments/member/{member-id}/create-desc")
    public Result appointmentsByMemberOrderByCreateDesc(@PathVariable("member-id") final Long id) {
        List<AppointmentDto> appointmentDtoList = appointmentService.findByMemberIdDESC(id)
                .stream().map(AppointmentDto::new)
                .collect(Collectors.toList());

        return new Result(appointmentDtoList);
    }


    @Data
    @AllArgsConstructor
    static class AppointmentDto {
        private String hospital;
        private String dept;
        private String comment;
        private String appointName;
        private String appointPhonenumber;

        public AppointmentDto(final Appointment appointment) {
            this.hospital = appointment.getHospital().getName();
            this.dept = appointment.getDept().toString();
            this.comment = appointment.getComment();
            this.appointName = appointment.getAppointName();
            this.appointPhonenumber = appointment.getAppointPhonenumber();
        }
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class UpdateAppointmentResponse {
        private Long id;
    }

    @Data
    static class UpdateAppointmentRequest {
        private String dept;
        private String comment;
        private String appointName;
        private String appointPhonenumber;
    }


    @Data
    @AllArgsConstructor
    static class DeleteAppointmentResponse {
        private Long id;
        private String status;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CreateAppointmentRequest {
        private Long memberId;
        private Long hospitalId;
        private String dept;
        private String comment;
        private String appointName;
        private String appointPhonenumber;
    }

    @Data
    @AllArgsConstructor
    static class CreateAppointmentResponse {
        private Long id;
    }
}
