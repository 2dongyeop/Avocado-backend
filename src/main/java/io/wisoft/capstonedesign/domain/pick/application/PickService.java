package io.wisoft.capstonedesign.domain.pick.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.persistence.PickRepository;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullPickException;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
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
    public Long save(final CreatePickRequest request) {

        //엔티티 조회
        Member member = memberService.findOne(request.getMemberId());
        Hospital hospital = hospitalService.findById(request.getHospitalId());
        Pick pick = Pick.createPick(member, hospital);

        pickRepository.save(pick);
        return pick.getId();
    }

    /**
     * 찜하기 취소
     */
    @Transactional
    public void cancelPick(final Long pickId) {
        Pick pick = findById(pickId);
        pick.cancel();
    }

    /* 조회 로직 */
    public Pick findById(final Long pickId) {
        return pickRepository.findById(pickId).orElseThrow(NullPickException::new);
    }

    public List<Pick> findByMemberId(final Long memberId) {
        return pickRepository.findByMemberId(memberId);
    }

    public List<Pick> findByMemberIdOrderByCreateAsc(final Long memberId) {
        return pickRepository.findByMemberIdOrderByCreateAtAsc(memberId);
    }

    public List<Pick> findByMemberIdOrderByCreateDesc(final Long memberId) {
        return pickRepository.findByMemberIdOrderByCreateAtDesc(memberId);
    }
}
