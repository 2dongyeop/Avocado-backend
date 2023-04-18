package io.wisoft.capstonedesign.domain.auth.web;


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

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    /** 회원 가입 */
    @PostMapping("/api/auth/signup/members")
    public CreateMemberResponse signupMember(@RequestBody @Valid final CreateMemberRequest request) {

        if (!request.password1().equals(request.password2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }
        return new CreateMemberResponse(authService.signUpMember(request));
    }


    /** 회원 로그인 */
    @PostMapping("/api/auth/login/members")
    public ResponseEntity<TokenResponse> loginMember(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginMember(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }


    /**
     * 로그아웃
     */
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


    /** 의료진 가입 */
    @PostMapping("/api/auth/signup/staff")
    public CreateStaffResponse signupStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        if (!request.password1().equals(request.password2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }

        return new CreateStaffResponse(authService.signUpStaff(request));
    }


    /** 의료진 로그인 */
    @PostMapping("/api/auth/login/staff")
    public ResponseEntity<TokenResponse> loginStaff(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginStaff(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }
}
