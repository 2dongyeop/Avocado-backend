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
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    /*
     * 회원가입
     */
    @Transactional
    public Long signUpMember(final CreateMemberRequest request) {

        validateAuthenticateCode(request.getEmail(), request.getCode());
        validateDuplicateMember(request);

        final Member member = Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encryptHelper.encrypt(request.getPassword1()))
                .phoneNumber(request.getPhonenumber())
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    private void validateAuthenticateCode(final String email, final String code) {
        MailAuthentication mail = mailAuthenticationRepository.findByEmail(email).orElseThrow(IllegalValueException::new);

        if (!mail.getCode().equals(code)) {
            throw new IllegalValueException("이메일 인증 코드가 올바르지 않습니다.");
        }
    }

    private void validateDuplicateMember(final CreateMemberRequest request) {
        final List<Member> validateMemberByEmail = memberRepository.findValidateMemberByEmail(request.getEmail());
        final List<Member> validateMemberByNickname = memberRepository.findValidateMemberByNickname(request.getNickname());

        if (!validateMemberByEmail.isEmpty() || !validateMemberByNickname.isEmpty()) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /** 로그인 */
    public String loginMember(final LoginRequest request) {

        final Member member = memberRepository.findMemberByEmail(request.getEmail())
                .orElseThrow(NullMemberException::new);

        if (!encryptHelper.isMatch(request.getPassword(), member.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(member.getNickname());
    }


    /**
     * 의료진가입
     */
    @Transactional
    public Long signUpStaff(final CreateStaffRequest request) {
        validateAuthenticateCode(request.getEmail(), request.getCode());
        validateDuplicateStaff(request);

        //엔티티 조회
        final Hospital hospital = hospitalService.findById(request.getHospitalId());

        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name(request.getName())
                .email(request.getEmail())
                .password(encryptHelper.encrypt(request.getPassword1()))
                .license_path(request.getLicensePath())
                .dept(HospitalDept.valueOf(request.getDept()))
                .build();

        staffRepository.save(staff);
        return staff.getId();
    }

    private void validateDuplicateStaff(final CreateStaffRequest request) {
        final List<Staff> staffList = staffRepository.findValidateByEmail(request.getEmail());
        if (!staffList.isEmpty()) throw new DuplicateStaffException();
    }


    /**
     * 의료진 로그인
     */
    public String loginStaff(final LoginRequest request) {

        final Staff staff = staffRepository.findStaffByEmail(request.getEmail()).orElseThrow(NullStaffException::new);

        if (!encryptHelper.isMatch(request.getPassword(), staff.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(staff.getName());
    }
}
