package io.wisoft.capstonedesign.domain.auth.web;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffResponse;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.jwt.RedisJwtBlackList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor authExtractor;
    private final RedisJwtBlackList redisJwtBlackList;

    @SwaggerApi(summary = "회원 가입", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/signup/members")
    public ResponseEntity<CreateMemberResponse> signupMember(@RequestBody @Valid final CreateMemberRequest request) {

        log.info("CreateMemberRequest[{}]", request);

        validatePassword(request.password1(), request.password2());
        final Long id = authService.signUpMember(request);

        if (id == null) {
            log.info("login fail");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CreateMemberResponse(id));
    }


    @SwaggerApi(summary = "회원 로그인", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/login/members")
    public ResponseEntity<LoginResponse> loginMember(@RequestBody @Valid final LoginRequest request) {

        log.info("LoginRequest[{}]", request);
        return ResponseEntity.ok(authService.loginMember(request));
    }


    @SwaggerApi(summary = "로그아웃", implementation = ResponseEntity.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/logout")
    public ResponseEntity<String> logoutMember(final HttpServletRequest request) {

        final String accessToken = authExtractor.extract(request, "Bearer");
        final String email = jwtTokenProvider.getSubject(accessToken);

        log.info("accessToken[{}], email[{}]", accessToken, email);

        redisJwtBlackList.addToBlackList(email);

        return ResponseEntity.ok("logout success");
    }

    @SwaggerApi(summary = "의료진 가입", implementation = CreateStaffResponse.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/signup/staff")
    public CreateStaffResponse signupStaff(
            @RequestBody @Valid final CreateStaffRequest request) {

        log.info("CreateStaffRequest[{}]", request);

        validatePassword(request.password1(), request.password2());
        return new CreateStaffResponse(authService.signUpStaff(request));
    }


    @SwaggerApi(summary = "의료진 로그인", implementation = ResponseEntity.class)
    @SwaggerApiFailWithoutAuth
    @PostMapping("/login/staff")
    public ResponseEntity<LoginResponse> loginStaff(@RequestBody @Valid final LoginRequest request) {

        log.info("LoginRequest[{}]", request);
        return ResponseEntity.ok(authService.loginStaff(request));
    }

    private void validatePassword(final String password1, final String confirmPassword) throws IllegalValueException {
        if (!password1.equals(confirmPassword)) {
            log.info("password1[{}], confirmPassword[{}] not valid", password1, confirmPassword);
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.", ErrorCode.ILLEGAL_PASSWORD);
        }
    }
}
