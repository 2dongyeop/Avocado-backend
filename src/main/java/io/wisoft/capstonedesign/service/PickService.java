package io.wisoft.capstonedesign.service;

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

    /**
     * 찜하기 생성
     */
    @Transactional
    public Long save(Pick pick) {
        pickRepository.save(pick);
        return pick.getId();
    }

    /**
     * 찜하기 취소
     */
    @Transactional
    public void cancelPick(Long pickId) {
        Pick pick = pickRepository.findOne(pickId);
        pick.cancel();
    }

    /* 조회 로직 */
    public Pick findOne(Long pickId) {

        Pick getPick = pickRepository.findOne(pickId);
        if (getPick == null) {
            throw new NullPickException("해당 찜하기 정보가 존재하지 않습니다.");
        }
        return getPick;
    }

    public List<Pick> findByMemberIdASC(Long memberId) {
        return pickRepository.findByMemberIdASC(memberId);
    }

    public List<Pick> findByMemberIdDESC(Long memberId) {
        return pickRepository.findByMemberIdDESC(memberId);
    }
}
