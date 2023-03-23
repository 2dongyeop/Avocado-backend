package io.wisoft.capstonedesign.member;

import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUp(final Member member) {

        //comment: 회원 중복 검증
        validateDuplicateMember(member);
        memberRepository.signUp(member);
        return member.getId();
    }

    private void validateDuplicateMember(final Member member) {
        List<Member> findMembersByEmail = memberRepository.findByEmail(member.getEmail());
        List<Member> findMembersByNickname = memberRepository.findByNickname(member.getNickname());
        if (findMembersByEmail.size() != 0 || findMembersByNickname.size() != 0) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long memberId, final String oldPassword, final String newPassword) {

        Member member = findOne(memberId);
        validateMemberPassword(member, oldPassword);

        member.updatePassword(newPassword);
    }

    private void validateMemberPassword(final Member member, final String oldPassword) {

        if (!member.getPassword().equals(oldPassword)) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }

    /* 회원 닉네임 수정 */
    @Transactional
    public void updateMemberNickname(final Long memberId, final String newNickname) {

        Member member = findOne(memberId);
        member.updateNickname(newNickname);
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(final Long memberId, final String newPhotoPath) {

        Member member = findOne(memberId);
        member.uploadPhotoPath(newPhotoPath);
    }

    /* 회원 탈퇴 */
    @Transactional
    public void deleteMember(final Long memberId) {

        Member member = findOne(memberId);
        member.delete();
    }


    /*
     * 회원 조회
     */
    public Member findOne(final Long memberId) {

        Member getMember = memberRepository.findOne(memberId);
        if (getMember == null) {
            throw new NullMemberException("해당 회원 정보가 존재하지 않습니다.");
        }
        return getMember;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public List<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
