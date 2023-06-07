package io.wisoft.capstonedesign.domain.payment.persistence;


import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findByAppointment(final Appointment appointment);
}
