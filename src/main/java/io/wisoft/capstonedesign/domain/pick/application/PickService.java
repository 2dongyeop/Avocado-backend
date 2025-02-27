package io.wisoft.capstonedesign.domain.pick.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.persistence.PickRepository;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        final Member member = memberService.findById(request.memberId());
        log.info("member[{}]", member);

        final Hospital hospital = hospitalService.findById(request.hospitalId());
        log.info("hospital[{}]", hospital);

        final Pick pick = Pick.createPick(member, hospital);
        log.info("pick[{}]", pick);


        pickRepository.save(pick);
        return pick.getId();
    }

    /**
     * 찜하기 취소
     */
    @Transactional
    public void cancelPick(final Long pickId) {
        pickRepository.delete(findById(pickId));
    }

    /* 조회 로직 */
    public Pick findById(final Long pickId) {
        return pickRepository.findById(pickId).orElseThrow(() -> {
            log.info("pickId[{}] not found", pickId);
            return new NotFoundException("찜하기 조회 실패");
        });
    }

    /**
     * 상세조회
     */
    public Pick findDetailById(final Long pickId) {
        return pickRepository.findDetailById(pickId).orElseThrow(() -> {
            log.info("pickId[{}] not found", pickId);
            return new NotFoundException("찜하기 조회 실패");
        });
    }
}
