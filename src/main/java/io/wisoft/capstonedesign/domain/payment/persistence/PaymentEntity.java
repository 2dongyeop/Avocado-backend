package io.wisoft.capstonedesign.domain.payment.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String pg;
    private String paymentMethod;
    private String paymentName;
    private String buyerEmail;
    private String buyerName;

    @OneToOne
    @JoinColumn(name = "appt_id")
    private Appointment appointment;

    public static PaymentEntity createPaymentEntity(
            final String pg,
            final String paymentMethod,
            final String paymentName,
            final String buyerEmail,
            final String buyerName) {

        final PaymentEntity payment = new PaymentEntity();
        payment.pg = pg;
        payment.paymentMethod = paymentMethod;
        payment.paymentName = paymentName;
        payment.buyerEmail = buyerEmail;
        payment.buyerName = buyerName;

        return payment;
    }
}
