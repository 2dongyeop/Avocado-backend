package io.wisoft.capstonedesign.domain.payment.web;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.payment.application.PaymentService;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final AppointmentService appointmentService;

    @Value("${iamport.api-key}")
    private String API_KEY;

    @Value("${iamport.api-secret}")
    private String API_SECRET;

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }


    @PostMapping("/payment/callback")
    public ResponseEntity<?> callbackReceived(@RequestBody final Map<String, Object> model) {

        //응답 header 생성
        final HttpHeaders responseHeaders = makeHttpHeader();

        try {

            final String imp_uid = (String) model.get("imp_uid");
            final String merchant_uid = (String) model.get("merchant_uid");
            final boolean success = (boolean) model.get("success");
            final String errorMsg = (String) model.get("error_msg");

            if (!success) {
                log.error(errorMsg);
                return new ResponseEntity<>(errorMsg, responseHeaders, HttpStatus.OK);
            }

            validateAppointmentPayStatus(Long.valueOf(merchant_uid));
            printLogByRequest(imp_uid, merchant_uid, success);


            final IamportClient iamportClient = new IamportClient(API_KEY, API_SECRET);
            final Payment payment = iamportClient.paymentByImpUid(imp_uid).getResponse();

            final Long savedId = paymentService.save(payment);
            return new ResponseEntity<>(savedId, responseHeaders, HttpStatus.OK);

        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateAppointmentPayStatus(final Long appointmentId) {
        final Appointment appointment = appointmentService.findById(appointmentId);

        if (appointment.getPayStatus() == PayStatus.COMPLETED) {
            throw new IllegalValueException("이미 결제된 예약입니다.");
        }
    }

    private void printLogByRequest(final String imp_uid, final String merchant_uid, final boolean success) {
        log.info("-----payment callback received-----");
        log.info("imp_uid = " + imp_uid);
        log.info("merchant_uid = " + merchant_uid);
        log.info("success = " + success);
    }

    private HttpHeaders makeHttpHeader() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return responseHeaders;
    }

}
