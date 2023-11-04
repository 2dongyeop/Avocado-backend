package io.wisoft.capstonedesign.domain.payment.web;

import io.wisoft.capstonedesign.domain.payment.application.PaymentService;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class RefundController {

    @Value("${iamport.api-key}")
    private String API_KEY;

    @Value("${iamport.api-secret}")
    private String API_SECRET;
    private final String GET_TOKEN_URL = "https://api.iamport.kr/users/getToken";
    private final String CANCEL_REQUEST_URL = "https://api.iamport.kr/payments/cancel";
    private final AuthorizationExtractor extractor;
    private final PaymentService paymentService;


    @GetMapping("/token")
    public ResponseEntity<String> getToken() throws IOException, JSONException {

        final HttpURLConnection conn = getTokenConnection();

        final JSONObject obj = getJsonObject();

        sendRequest(conn, obj);

        final int responseCode = getResponseCode(conn);
        log.info("resposneCode[{}]", responseCode);

        if (responseCode != 200) {
            return ResponseEntity.badRequest().build();
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        final String accessToken = getResponse(br);
        log.debug("accessToken[{}]", accessToken);

        disconnect(conn, br);
        return ResponseEntity.ok(accessToken);
    }


    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> refund(
            final HttpServletRequest httpServletRequest,
            @RequestBody final String merchantUid,
            @PathVariable final Long id) {

        final String token = extractor.extract(httpServletRequest, "Bearer");

        return executePaymentCancel(token, merchantUid, id);
    }


    public void refundWithToken(
            final String token,
            final String merchantUid,
            final Long appointmentId) {

        executePaymentCancel(token, merchantUid, appointmentId);
    }

    private ResponseEntity<String> executePaymentCancel(final String token, final String merchantUid, final Long appointmentId) {
        final HttpHeaders headers = getHttpHeaders(token);

        final JSONObject jsonObject = getJsonObject(merchantUid);

        final ResponseEntity<String> response = sendCancelRequest(headers, jsonObject);
        log.debug("uid : {} 의 예약이 취소되었습니다.", merchantUid);

        //취소 되었으니 Payment & Appointment의 상태를 다시 결제 전으로 바꾸기
        paymentService.refund(appointmentId);
        return response;
    }


    @NotNull
    private ResponseEntity<String> sendCancelRequest(final HttpHeaders headers, final JSONObject jsonObject) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
        return restTemplate.exchange(
                CANCEL_REQUEST_URL,
                HttpMethod.POST,
                entity,
                String.class);
    }

    @NotNull
    private JSONObject getJsonObject(final String merchantUid) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merchant_uid", merchantUid);
        } catch (JSONException e) {
            log.warn(e);
        }
        return jsonObject;
    }

    @NotNull
    private HttpHeaders getHttpHeaders(final String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", token);
        return headers;
    }


    private void disconnect(final HttpURLConnection conn, final BufferedReader br) throws IOException {
        br.close();
        conn.disconnect();
    }

    @NotNull
    private String getResponse(final BufferedReader br) throws IOException {
        final StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        return sb.toString();
    }

    private int getResponseCode(final HttpURLConnection conn) throws IOException {
        final int responseCode = conn.getResponseCode();
        log.debug("responseCode[{}]", responseCode);

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
    private HttpURLConnection getTokenConnection() throws IOException {
        final URL url = new URL(GET_TOKEN_URL);
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
}
