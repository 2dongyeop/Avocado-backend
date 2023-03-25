package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
import io.wisoft.capstonedesign.global.enumeration.status.PickStatus;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullPickException;
import io.wisoft.capstonedesign.domain.pick.persistence.PickRepository;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PickServiceTest {

    @Autowired EntityManager em;
    @Autowired
    PickService pickService;
    @Autowired PickRepository pickRepository;
    
    //찜하기 저장
    @Test
    public void 찜하기_저장() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "04212345678", "대전시 유성구", "365일 연중무휴");
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());

        //when -- 동작
        Long saveId = pickService.save(request);

        //then -- 검증
        Pick pick = pickRepository.findOne(saveId);
        Assertions.assertThat(pick.getStatus()).isEqualTo(PickStatus.COMPLETE);
        Assertions.assertThat(pick.getHospital().getName()).isEqualTo(hospital.getName());
        Assertions.assertThat(pick.getMember().getNickname()).isEqualTo(member.getNickname());
    }
    
    //찜하기 취소
    @Test
    public void 찜하기_취소() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "04212345678", "대전시 유성구", "365일 연중무휴");
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());
        Long saveId = pickService.save(request);

        //when -- 동작
        pickService.cancelPick(saveId);

        //then -- 검증
        Assertions.assertThat(pickService.findOne(saveId).getStatus()).isEqualTo(PickStatus.CANCEL);
    }

    
    //찜하기 중복 취소 요청
    @Test(expected = IllegalStateException.class)
    public void 찜하기_취소_요청_중복() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "04212345678", "대전시 유성구", "365일 연중무휴");
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());
        Long saveId = pickService.save(request);

        //when -- 동작
        pickService.cancelPick(saveId);
        pickService.cancelPick(saveId);

        //then -- 검증
        fail("중복 찜하기 취소 요청으로 인한 예외가 발생해야 한다.");
    }

    //찜하기 단건 조회 실패
    @Test(expected = NullPickException.class)
    public void 찜하기_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        pickService.findOne(100L);

        //then -- 검증
        fail("해당 pickId에 일치하는 찜하기 정보가 없어 예외가 발생해야 한다.");
    }
}