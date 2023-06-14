package io.wisoft.capstonedesign.domain.auth.application;


import io.wisoft.capstonedesign.domain.auth.persistence.DBMailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
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
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateEmailException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateNicknameException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final int LOGIN_EXPIRED_TIME = 1 * 60 * 60; //1 hour

    private final MemberRepository memberRepository;
    private final StaffRepository staffRepository;
    private final HospitalService hospitalService;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailAuthenticationRepository mailAuthenticationRepository;
    private final StringRedisTemplate redisTemplate;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUpMember(final CreateMemberRequest request) {

        try {
            validateEmailVerified(request.email());
            validateDuplicateNickname(request.nickname());

            final Member member = createMember(request);


            //회원 저장
            memberRepository.save(member);

            //인증을 위해 저장했던 이메일 삭제
            final var mailAuthentication = mailAuthenticationRepository.findByEmail(request.email()).get();
            mailAuthenticationRepository.delete(mailAuthentication);

            log.info("{}님이 회원가입을 하셨습니다.", member.getNickname());
            return member.getId();
        } catch (DuplicateEmailException duplicateException) {
            duplicateException.printStackTrace();
            return null;
        }
    }


    /**
     * 로그인
     */
    @Transactional
    public String loginMember(final LoginRequest request) {

        String token = null;

        try {

            final Member member = memberRepository.findMemberByEmail(request.email())
                    .orElseThrow(NotFoundException::new);

            validatePassowrd(request, member.getPassword());

            token = jwtTokenProvider.createToken(member.getNickname());
            log.info("{}님이 로그인 하셨습니다.", member.getNickname());

            redisTemplate.opsForValue().set(
                    token, member.getNickname(),
                    LOGIN_EXPIRED_TIME,
                    TimeUnit.SECONDS
            );
            log.info("redis : 토큰{}을 1시간동안 저장합니다.", token);

        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return token;
    }


    /**
     * 의료진가입
     */
    @Transactional
    public Long signUpStaff(final CreateStaffRequest request) {
        validateEmailVerified(request.email());

        //엔티티 조회
        final Hospital hospital = hospitalService.findById(request.hospitalId());

        final Staff staff = createStaff(request, hospital);

        //의료진 저장
        staffRepository.save(staff);

        //이메일 인증을 위해 저장했던 이메일 삭제
        final var mailAuthentication = mailAuthenticationRepository.findByEmail(request.email()).get();
        mailAuthenticationRepository.delete(mailAuthentication);

        log.info("{}님이 가입 하셨습니다.", staff.getName());
        return staff.getId();
    }


    /**
     * 의료진 로그인
     */
    @Transactional
    public String loginStaff(final LoginRequest request) {

        final Staff staff = staffRepository.findStaffByEmail(request.email()).orElseThrow(NotFoundException::new);

        validatePassowrd(request, staff.getPassword());

        final String token = jwtTokenProvider.createToken(staff.getName());
        log.info("{}님이 로그인 하셨습니다.", staff.getName());

        redisTemplate.opsForValue().set(
                token, staff.getName(),
                LOGIN_EXPIRED_TIME,
                TimeUnit.SECONDS
        );
        log.info("redis : 토큰{}을 1시간동안 저장합니다.", token);

        return token;
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
        if (memberRepository.findValidateMemberByNickname(nickname).size() > 0) {
            throw new DuplicateNicknameException("닉네임 중복", ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateEmailVerified(final String email) throws IllegalStateException {
        final DBMailAuthentication mail = mailAuthenticationRepository.findByEmail(email).orElseThrow(NotFoundException::new);

        if (!mail.isVerified()) {
            throw new IllegalValueException("이메일 인증을 완료해주세요.", ErrorCode.ILLEGAL_STATE);
        }
    }

    private void validatePassowrd(final LoginRequest request, final String member) throws IllegalValueException {
        if (!encryptHelper.isMatch(request.password(), member)) {
            log.error("비밀번호가 일치하지 않습니다.");
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
