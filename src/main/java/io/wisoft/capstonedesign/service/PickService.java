package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.domain.Pick;
import io.wisoft.capstonedesign.exception.nullcheck.NullPickException;
import io.wisoft.capstonedesign.repository.PickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PickService {

    private final PickRepository pickRepository;
    private final MemberService memberService;
    private final HospitalService hospitalService;

    /**
     * 찜하기 생성
     */
    @Transactional
    public Long save(final Long memberId, final Long hospitalId) {

        //엔티티 조회
        Member member = memberService.findOne(memberId);
        Hospital hospital = hospitalService.findOne(hospitalId);

        Pick pick = Pick.createPick(member, hospital);

        pickRepository.save(pick);
        return pick.getId();
    }

    /**
     * 찜하기 취소
     */
    @Transactional
    public void cancelPick(final Long pickId) {
        Pick pick = pickRepository.findOne(pickId);
        pick.cancel();
    }

    /* 조회 로직 */
    public Pick findOne(final Long pickId) {

        Pick getPick = pickRepository.findOne(pickId);
        if (getPick == null) {
            throw new NullPickException("해당 찜하기 정보가 존재하지 않습니다.");
        }
        return getPick;
    }

    public List<Pick> findByMemberId(final Long memberId) {
        return pickRepository.findByMemberId(memberId);
    }

    public List<Pick> findByMemberIdOrderByCreateAsc(final Long memberId) {
        return pickRepository.findByMemberIdOrderByCreateAsc(memberId);
    }

    public List<Pick> findByMemberIdOrderByCreateDesc(final Long memberId) {
        return pickRepository.findByMemberIdOrderByCreateDesc(memberId);
    }
}
