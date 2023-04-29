package io.wisoft.capstonedesign.domain.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.MailObject;
import io.wisoft.capstonedesign.domain.auth.application.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이메일 인증")
@RestController
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/mail/certification")
    public ResponseEntity<String> certificateEmail(@RequestBody final MailObject mailObject) {
        emailService.sendCertificationCode(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "회원 비밀번호 찾기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/mail/member/password")
    public ResponseEntity<String> resetMemberPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetMemberPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "의료진 비밀번호 찾기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/mail/staff/password")
    public ResponseEntity<String> resetStaffPassword(@RequestBody final MailObject mailObject) {
        emailService.sendResetStaffPassword(mailObject.email());
        return ResponseEntity.ok("success");
    }
}
