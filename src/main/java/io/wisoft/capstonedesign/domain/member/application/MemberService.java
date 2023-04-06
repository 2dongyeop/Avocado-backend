package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.config.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberNicknameRequest;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPasswordRequest;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPhotoPathRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EncryptHelper encryptHelper;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUp(final CreateMemberRequest request) {

        //comment: 회원 중복 검증
        validateDuplicateMember(request);

        Member member = Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encryptHelper.encrypt(request.getPassword1()))
                .phoneNumber(request.getPhonenumber())
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(final CreateMemberRequest request) {
        List<Member> findMembersByEmail = memberRepository.findByEmail(request.getEmail());
        List<Member> findMembersByNickname = memberRepository.findAllByNickname(request.getNickname());

        if (!findMembersByEmail.isEmpty() || !findMembersByNickname.isEmpty()) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long memberId, final UpdateMemberPasswordRequest request) {

        Member member = findOne(memberId);
        validateMemberPassword(member, request);

        member.updatePassword(encryptHelper.encrypt(request.getNewPassword()));
    }

    private void validateMemberPassword(final Member member, final UpdateMemberPasswordRequest request) {

        if (!encryptHelper.isMatch(request.getOldPassword(), member.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /* 회원 닉네임 수정 */
    @Transactional
    public void updateMemberNickname(final Long memberId, final UpdateMemberNicknameRequest request) {

        Member member = findOne(memberId);
        member.updateNickname(request.getNickname());
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(final Long memberId, final UpdateMemberPhotoPathRequest request) {

        Member member = findOne(memberId);
        member.uploadPhotoPath(request.getPhotoPath());
    }


    /* 회원 탈퇴 */
    @Transactional
    public void deleteMember(final Long memberId) {
        Member member = findOne(memberId);
        memberRepository.delete(member);
    }


    /*
     * 회원 조회
     */
    public Member findOne(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NullMemberException::new);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findByNickname(final String nickname) {
        return memberRepository.findAllByNickname(nickname);
    }

    public List<Member> findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }
}
