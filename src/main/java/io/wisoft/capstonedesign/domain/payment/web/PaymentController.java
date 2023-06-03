package io.wisoft.capstonedesign.domain.appointment.web;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import io.wisoft.capstonedesign.domain.appointment.application.PaymentService;
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

            printLogByRequest(imp_uid, merchant_uid, success);

            if (success == true) {

                final IamportClient iamportClient = new IamportClient(API_KEY, API_SECRET);
                final Payment payment = iamportClient.paymentByImpUid(imp_uid).getResponse();

                final Long savedId = paymentService.save(payment);
                return new ResponseEntity<>(savedId, responseHeaders, HttpStatus.OK);

            } else {
                log.error(errorMsg);
                return new ResponseEntity<>(errorMsg, responseHeaders, HttpStatus.OK);
            }

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void printLogByRequest(final String imp_uid, final String merchant_uid, final boolean success) {
        log.info("-----payment callback received-----" +
                "\nimp_uid = " + imp_uid +
                "\nmerchant_uid = " + merchant_uid +
                "\nsuccess = " + success);
    }

    private HttpHeaders makeHttpHeader() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return responseHeaders;
    }

}
