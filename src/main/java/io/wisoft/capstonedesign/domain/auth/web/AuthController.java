package io.wisoft.capstonedesign.domain.auth.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.TokenResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffResponse;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/auth/signup/members")
    public CreateMemberResponse signupMember(@RequestBody @Valid final CreateMemberRequest request) {

        if (!request.password1().equals(request.password2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }
        return new CreateMemberResponse(authService.signUpMember(request));
    }


    @Operation(summary = "회원 로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/auth/login/members")
    public ResponseEntity<TokenResponse> loginMember(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginMember(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }


    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/auth/logout")
    public ResponseEntity<String> logoutMember(HttpServletRequest request) {
        //TODO
        boolean result = authService.logout(request);

        if (result) {
            return ResponseEntity.ok("logout success");
        } else {
            return ResponseEntity.badRequest().body("bad request");
        }
    }


    @Operation(summary = "의료진 가입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/auth/signup/staff")
    public CreateStaffResponse signupStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        if (!request.password1().equals(request.password2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }

        return new CreateStaffResponse(authService.signUpStaff(request));
    }


    @Operation(summary = "의료진 로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/auth/login/staff")
    public ResponseEntity<TokenResponse> loginStaff(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginStaff(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }
}
