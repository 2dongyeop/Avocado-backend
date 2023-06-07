package io.wisoft.capstonedesign.domain.payment.application;

import com.siot.IamportRestClient.response.Payment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.payment.persistence.PaymentEntity;
import io.wisoft.capstonedesign.domain.payment.persistence.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //해당 예약건 결제
        appointment.payment();

        //결제 정보 생성(매핑) 후 저장
        final PaymentEntity paymentEntity = paymentToPaymentEntity(payment);

        return paymentRepository.save(paymentEntity).getId();
    }

    private PaymentEntity paymentToPaymentEntity(final Payment payment) {
        final PaymentEntity paymentEntity = PaymentEntity.createPaymentEntity(
                payment.getPgProvider(),
                payment.getPayMethod(),
                "아보카도 병원 예약금",
                payment.getBuyerEmail(),
                payment.getBuyerName()
        );
        return paymentEntity;
    }

    @Transactional
    public Long refund(final Long appointmentId) {

        //예약 정보 조회
        final Appointment appointment = appointmentService.findById(appointmentId);

        //해당 예약건 결제 취소 상태로 바꾸기
        appointment.refund();

        //결제 정보 조회
        final PaymentEntity paymentEntity = paymentRepository.findByAppointment(appointment);
        paymentEntity.refund();

        return paymentEntity.getId();
    }
}
