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
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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


    @PostMapping("/payment")
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


//    @PostMapping("/refund")
//    public ResponseEntity<?> refundReceived(@RequestBody final Map<String, Object> model) {
//
//
//    }


    @GetMapping("/token")
    public ResponseEntity<?> getToken() throws IOException, JSONException {

        final HttpURLConnection conn = getHttpURLConnection();

        final JSONObject obj = getJsonObject();

        sendRequest(conn, obj);

        final int responseCode = getResponseCode(conn);

        if (responseCode != 200) {
            return ResponseEntity.badRequest().build();
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        getResponse(br);

        disconnect(conn, br);
        return ResponseEntity.ok().build();
    }

    private void disconnect(final HttpURLConnection conn, final BufferedReader br) throws IOException {
        br.close();
        conn.disconnect();
    }

    @NotNull
    private StringBuilder getResponse(final BufferedReader br) throws IOException {
        final StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        System.out.println(sb);
        return sb;
    }

    private int getResponseCode(final HttpURLConnection conn) throws IOException {
        final int responseCode = conn.getResponseCode();
        System.out.println("responseCode = " + responseCode);

        return responseCode;
    }

    private void sendRequest(final HttpURLConnection conn, final JSONObject obj) throws IOException {
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(obj.toString());
        bw.flush();
        bw.close();
    }

    @NotNull
    private JSONObject getJsonObject() throws JSONException {

        //자신의 키를 JSON 형태로 변환
        final JSONObject obj = new JSONObject();
        obj.put("imp_key", API_KEY);
        obj.put("imp_secret", API_SECRET);
        return obj;
    }

    @NotNull
    private HttpURLConnection getHttpURLConnection() throws IOException {
        final URL url = new URL("https://api.iamport.kr/users/getToken");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //요청방식 : POST
        conn.setRequestMethod("POST");

        //Header 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        //Data 설정 - OutputStream으로 데이터를 넘기겠다
        conn.setDoOutput(true);
        return conn;
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
