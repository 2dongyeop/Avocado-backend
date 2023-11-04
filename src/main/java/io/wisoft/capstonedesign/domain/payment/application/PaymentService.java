package io.wisoft.capstonedesign.domain.payment.application;

import com.siot.IamportRestClient.response.Payment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.payment.persistence.PaymentEntity;
import io.wisoft.capstonedesign.domain.payment.persistence.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentService appointmentService;

    @Transactional
    public Long save(final Long appointmentId, final Payment payment) {
        //예약 정보 조회
        final Appointment appointment = appointmentService.findById(appointmentId);
        log.info("appointment[{}]", appointment);

        //해당 예약건 결제
        appointment.payment();
        log.info("appointment[{}] pay success", appointment);

        //결제 정보 생성(매핑) 후 저장
        final PaymentEntity paymentEntity = paymentToPaymentEntity(payment);
        log.info("paymentEntity[{}]", paymentEntity);

        return paymentRepository.save(paymentEntity).getId();
    }

    private PaymentEntity paymentToPaymentEntity(final Payment payment) {
        return PaymentEntity.createPaymentEntity(
                payment.getPgProvider(),
                payment.getPayMethod(),
                "아보카도 병원 예약금",
                payment.getBuyerEmail(),
                payment.getBuyerName()
        );
    }

    @Transactional
    public Long refund(final Long appointmentId) {

        //예약 정보 조회
        final Appointment appointment = appointmentService.findById(appointmentId);
        log.info("appointment[{}]", appointment);

        //해당 예약건 결제 취소 상태로 바꾸기
        appointment.refund();
        log.info("appointment[{}] refund success", appointment);

        //결제 정보 조회
        final PaymentEntity paymentEntity = paymentRepository.findByAppointment(appointment);
        log.info("paymentEntity[{}]", paymentEntity);

        paymentEntity.refund();
        log.info("paymentEntity[{}] refund success", paymentEntity);

        return paymentEntity.getId();
    }
}
