package io.wisoft.capstonedesign.domain.pick.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullPickException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PickServiceTest {

    @Autowired
    EntityManager em;
    @Autowired PickService pickService;

    @Test
    public void 찜하기_저장() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());

        //when -- 동작
        Long saveId = pickService.save(request);

        //then -- 검증
        Pick pick = pickService.findById(saveId);
        Assertions.assertThat(pick.getHospital().getName()).isEqualTo(hospital.getName());
        Assertions.assertThat(pick.getMember().getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void 찜하기_취소() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());
        Long saveId = pickService.save(request);

        //when -- 동작
        pickService.cancelPick(saveId);

        //then -- 검증
        assertThrows(NullPickException.class, () -> {
            pickService.findById(saveId);
        });
    }


    @Test
    public void 찜하기_취소_요청_중복() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());
        Long saveId = pickService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullPickException.class, () -> {
            pickService.cancelPick(saveId);
            pickService.cancelPick(saveId);
        });
    }


    @Test
    public void 찜하기_단건_조회() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());

        Long saveId = pickService.save(request);

        //when -- 동작
        Pick pick = pickService.findById(saveId);

        //then -- 검증
        Assertions.assertThat(pick).isNotNull();
        Assertions.assertThat(pick.getMember().getNickname()).isEqualTo("nick1");

    }

    @Test
    public void 찜하기_단건_조회_실패() throws Exception {
        //given -- 조건
        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreatePickRequest request = new CreatePickRequest(member.getId(), hospital.getId());

        Long saveId = pickService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullPickException.class, () -> {
            pickService.findById(100L);
        });
    }
}