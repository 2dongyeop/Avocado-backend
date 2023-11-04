package io.wisoft.capstonedesign.domain.payment.web;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.payment.application.PaymentService;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Controller //TODO : 나중에 프론트와 연동시 RestController 로 바꿀것
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RefundController refundController;
    private final AppointmentService appointmentService;


    @Value("${iamport.api-key}")
    private String API_KEY;

    @Value("${iamport.api-secret}")
    private String API_SECRET;

    @GetMapping
    public String payment() {
        return "payment";
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> savePayment(
            @RequestBody final Map<String, Object> model,
            @PathVariable(value = "id") final Long appointmentId) throws JSONException, IOException {

        //응답 header 생성
        final HttpHeaders responseHeaders = makeHttpHeader();

        final String impUid = (String) model.get("imp_uid");
        final String merchantUid = (String) model.get("merchant_uid");
        final boolean success = (boolean) model.get("success");
        final String errorMsg = (String) model.get("error_msg");

        log.info("impUid[{}], merchantUid[{}], success[{}], errorMsg[{}]", impUid, merchantUid, success, errorMsg);

        if (!success) {
            log.error(errorMsg);
            return new ResponseEntity<>(errorMsg, responseHeaders, HttpStatus.OK);
        }

        try {
            validateAppointmentPayStatus(appointmentId);

            final var iamportClient = new IamportClient(API_KEY, API_SECRET);
            final Payment payment = extractPayment(impUid, iamportClient);
            log.info("payment[{}]", payment);

            return new ResponseEntity<>(paymentService.save(appointmentId, payment), responseHeaders, HttpStatus.OK);

        } catch (IamportResponseException | IOException e) {

            log.info("error! in payment");
            log.error("{}", e);

            //예외 발생시 결제를 취소
            final String token = refundController.getToken().getBody();
            refundController.refundWithToken(token, merchantUid, appointmentId);

            return ResponseEntity.badRequest().build();
        }
    }

    private Payment extractPayment(final String imp_uid, final IamportClient iamportClient) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid).getResponse();
    }


    private void validateAppointmentPayStatus(final Long appointmentId) {
        final Appointment appointment = appointmentService.findById(appointmentId);
        log.info("appointment[{}]", appointment);

        if (appointment.getPayStatus() == PayStatus.COMPLETED) {
            log.info("appointment[{}] is already pay", appointment);
            throw new IllegalValueException("이미 결제된 예약입니다.", ErrorCode.ILLEGAL_STATE);
        }
    }

    private HttpHeaders makeHttpHeader() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        responseHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return responseHeaders;
    }
}
