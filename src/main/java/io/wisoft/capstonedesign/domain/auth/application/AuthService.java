package io.wisoft.capstonedesign.domain.auth.application;


import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
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
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMailException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final StaffRepository staffRepository;
    private final HospitalService hospitalService;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailAuthenticationRepository mailAuthenticationRepository;
    private final AuthorizationExtractor authExtractor;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUpMember(final CreateMemberRequest request) {

        validateEmailVerified(request.email());

        final Member member = Member.builder()
                .nickname(request.nickname())
                .email(request.email())
                .password(encryptHelper.encrypt(request.password1()))
                .phoneNumber(request.phonenumber())
                .build();

        validateDuplicateEmail(request.email());
        validateDuplicateNickname(request.nickname());
        memberRepository.save(member);

        log.info(member.getNickname() + "님이 회원가입을 하셨습니다.");
        return member.getId();
    }

    private void validateDuplicateNickname(final String nickname) {
        List<Member> memberList = memberRepository.findValidateMemberByNickname(nickname);
        if (memberList.size() > 0) {
            throw new DuplicateMemberException("닉네임 중복");
        }
    }

    private void validateDuplicateEmail(final String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new DuplicateMemberException("이메일 중복.");
        }

        Optional<Staff> staff = staffRepository.findByEmail(email);
        if (staff.isPresent()) {
            throw new DuplicateStaffException("이메일 중복");
        }
    }

    private void validateEmailVerified(final String email) {
        MailAuthentication mail = mailAuthenticationRepository.findByEmail(email).orElseThrow(NullMailException::new);

        if (!mail.isVerified()) {
            throw new IllegalStateException("이메일 인증을 완료해주세요.");
        }
    }


    /** 로그인 */
    public String loginMember(final LoginRequest request) {

        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(NullMemberException::new);

        if (!encryptHelper.isMatch(request.password(), member.getPassword())) {
            log.error("비밀번호가 일치하지 않습니다.");
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        log.info(member.getNickname() + "님이 로그인 하셨습니다.");
        return jwtTokenProvider.createToken(member.getNickname());
    }


    /** 로그아웃 */
    public boolean logout(final HttpServletRequest request) {

        final String token = authExtractor.extract(request, "Bearer");

        try {
            jwtTokenProvider.addBlackList(token);
            log.info(jwtTokenProvider.getSubject(token) + "님이 로그아웃 하셨습니다.");
            return true;
        } catch (IllegalAccessException exception) {
            /* do nothing */
            return false;
        }
    }


    /**
     * 의료진가입
     */
    @Transactional
    public Long signUpStaff(final CreateStaffRequest request) {
        validateEmailVerified(request.email());

        //엔티티 조회
        final Hospital hospital = hospitalService.findById(request.hospitalId());

        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name(request.name())
                .email(request.email())
                .password(encryptHelper.encrypt(request.password1()))
                .license_path(request.licensePath())
                .dept(HospitalDept.valueOf(request.dept()))
                .build();

        validateDuplicateEmail(request.email());
        staffRepository.save(staff);

        log.info(staff.getName() + "님이 가입 하셨습니다.");
        return staff.getId();
    }


    /**
     * 의료진 로그인
     */
    public String loginStaff(final LoginRequest request) {

        final Staff staff = staffRepository.findStaffByEmail(request.email()).orElseThrow(NullStaffException::new);

        if (!encryptHelper.isMatch(request.password(), staff.getPassword())) {
            log.error("비밀번호가 일치하지 않습니다.");
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        log.info(staff.getName() + "님이 로그인 하셨습니다.");
        return jwtTokenProvider.createToken(staff.getName());
    }
}
