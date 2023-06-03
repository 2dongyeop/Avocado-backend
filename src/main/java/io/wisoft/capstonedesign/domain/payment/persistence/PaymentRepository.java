package io.wisoft.capstonedesign.domain.payment.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
