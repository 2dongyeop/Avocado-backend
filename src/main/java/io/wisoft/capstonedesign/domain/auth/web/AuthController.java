package io.wisoft.capstonedesign.domain.auth.web;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.TokenResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffResponse;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import io.wisoft.capstonedesign.global.jwt.RedisJwtBlackList;
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
    private final AuthorizationExtractor authExtractor;
    private final RedisJwtBlackList redisJwtBlackList;


    @SwaggerApi(summary = "회원 가입", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/api/auth/signup/members")
    public ResponseEntity<CreateMemberResponse> signupMember(@RequestBody @Valid final CreateMemberRequest request) {

        validatePassword(request.password1(), request.password2());
        final Long id = authService.signUpMember(request);

        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CreateMemberResponse(id));
    }


    @SwaggerApi(summary = "회원 로그인", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/api/auth/login/members")
    public ResponseEntity<TokenResponse> loginMember(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginMember(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }


    @SwaggerApi(summary = "로그아웃", implementation = ResponseEntity.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/auth/logout")
    public ResponseEntity<String> logoutMember(HttpServletRequest request) throws IllegalAccessException {

        final String token = authExtractor.extract(request, "Bearer");
        redisJwtBlackList.addToBlackList(token);

        return ResponseEntity.ok("logout success");
    }


    @SwaggerApi(summary = "의료진 가입", implementation = CreateStaffResponse.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/api/auth/signup/staff")
    public CreateStaffResponse signupStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        validatePassword(request.password1(), request.password2());
        return new CreateStaffResponse(authService.signUpStaff(request));
    }


    @SwaggerApi(summary = "의료진 로그인", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/api/auth/login/staff")
    public ResponseEntity<TokenResponse> loginStaff(@RequestBody @Valid final LoginRequest request) {
        final String token = authService.loginStaff(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }

    private static void validatePassword(final String password1, final String confirmPassword) throws IllegalValueException {
        if (!password1.equals(confirmPassword)) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.", ErrorCode.ILLEGAL_PASSWORD);
        }
    }
}
