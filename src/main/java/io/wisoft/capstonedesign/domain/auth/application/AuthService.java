package io.wisoft.capstonedesign.domain.auth.application;


import io.wisoft.capstonedesign.domain.auth.persistence.DBMailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginResponse;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateNicknameException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    @Value("${security.jwt.token.refresh-expire-length}")
    private long REFRESH_TOKEN_EXPIRE_SECOND;

    @Value("${security.jwt.token.token-type}")
    private String tokenType;

    private final MemberRepository memberRepository;
    private final StaffRepository staffRepository;
    private final HospitalService hospitalService;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailAuthenticationRepository mailAuthenticationRepository;
    private final RedisAdapter redisAdapter;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUpMember(final CreateMemberRequest request) {

        validateEmailVerified(request.email());
        validateDuplicateNickname(request.nickname());

        final Member member = createMember(request);
        log.info("member[{}]", member);

        //회원 저장
        memberRepository.save(member);

        //인증을 위해 저장했던 이메일 삭제
        final var mailAuthentication = mailAuthenticationRepository.findByEmail(request.email()).get();
        mailAuthenticationRepository.delete(mailAuthentication);

        log.debug("{}님이 회원가입을 하셨습니다.", member.getNickname());
        return member.getId();
    }


    /**
     * 로그인
     */
    @Transactional
    public LoginResponse loginMember(final LoginRequest request) {

        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("회원 조회 실패"));

        validatePassword(request, member.getPassword());

        final String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        log.debug("accessToken[{}], refreshToken[{}]", accessToken, refreshToken);

        redisAdapter.setValue(member.getEmail(), refreshToken, REFRESH_TOKEN_EXPIRE_SECOND, TimeUnit.SECONDS);

        return new LoginResponse(member.getId(), tokenType, accessToken, refreshToken, member.getNickname());
    }


    /**
     * 의료진가입
     */
    @Transactional
    public Long signUpStaff(final CreateStaffRequest request) {
        validateEmailVerified(request.email());

        //엔티티 조회
        final Hospital hospital = hospitalService.findByHospitalName(request.hospitalName());

        final Staff staff = createStaff(request, hospital);
        log.info("staff[{}]", staff);

        //의료진 저장
        staffRepository.save(staff);

        //이메일 인증을 위해 저장했던 이메일 삭제
        final var mailAuthentication = mailAuthenticationRepository.findByEmail(request.email()).get();
        mailAuthenticationRepository.delete(mailAuthentication);

        return staff.getId();
    }


    /**
     * 의료진 로그인
     */
    @Transactional
    public LoginResponse loginStaff(final LoginRequest request) {

        final Staff staff = staffRepository.findStaffByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("의료진 조회 실패"));

        validatePassword(request, staff.getPassword());

        final String accessToken = jwtTokenProvider.createAccessToken(staff.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken(staff.getEmail());

        log.debug("accessToken[{}], refreshToken[{}]", accessToken, refreshToken);

        redisAdapter.setValue(staff.getEmail(), refreshToken, REFRESH_TOKEN_EXPIRE_SECOND, TimeUnit.SECONDS);

        return new LoginResponse(staff.getId(), tokenType, accessToken, refreshToken, staff.getName());
    }

    private Member createMember(final CreateMemberRequest request) {
        return Member.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(encryptHelper.encrypt(request.password1()))
                .phoneNumber(request.phonenumber())
                .build();
    }

    private void validateDuplicateNickname(final String nickname) throws DuplicateNicknameException {
        if (!memberRepository.findValidateMemberByNickname(nickname).isEmpty()) {
            log.info("nickname[{}] is duplicated", nickname);
            throw new DuplicateNicknameException("닉네임 중복", ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private void validateEmailVerified(final String email) throws IllegalStateException {
        final DBMailAuthentication mail = mailAuthenticationRepository.findByEmail(email).orElseThrow(() -> {

            log.info("email[{}] not exist", email);
            return new NotFoundException("이메일 인증 정보 조회 실패");
        });

        if (!mail.isVerified()) {
            log.info("email[{}] not verified", email);
            throw new IllegalValueException("이메일 인증을 완료해주세요.", ErrorCode.ILLEGAL_STATE);
        }
    }

    private void validatePassword(final LoginRequest request, final String member) throws IllegalValueException {
        if (!encryptHelper.isMatch(request.password(), member)) {
            log.info("비밀번호가 일치하지 않습니다.");
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.", ErrorCode.ILLEGAL_PASSWORD);
        }
    }

    private Staff createStaff(final CreateStaffRequest request, final Hospital hospital) {
        return Staff.builder()
                .hospital(hospital)
                .name(request.name())
                .email(request.email())
                .password(encryptHelper.encrypt(request.password1()))
                .license_path(request.licensePath())
                .dept(HospitalDept.valueOf(request.dept()))
                .build();
    }
}
