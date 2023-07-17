package io.wisoft.capstonedesign.domain.payment.persistence;

import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class PaymentEntityTest {

    @Test
    public void refund() throws Exception {
        //given -- 조건
        final PaymentEntity payment = getPaymentEntity();

        //when -- 동작
        payment.refund();
        
        //then -- 검증
        Assertions.assertThat(payment.getPayStatus()).isEqualTo(PayStatus.REFUND);
    }

    @NotNull
    private static PaymentEntity getPaymentEntity() {
        final PaymentEntity payment = PaymentEntity.createPaymentEntity(
                "pg",
                "paymentMethod",
                "paymentName",
                "buyerEmail",
                "buyerName"
        );
        return payment;
    }
}