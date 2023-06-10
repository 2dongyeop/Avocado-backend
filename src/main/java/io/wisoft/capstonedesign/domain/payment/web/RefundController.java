package io.wisoft.capstonedesign.domain.payment.web;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class RefundController {

    @Value("${iamport.api-key}")
    private String API_KEY;

    @Value("${iamport.api-secret}")
    private String API_SECRET;
    private final String GET_TOKEN_URL = "https://api.iamport.kr/users/getToken";


    @GetMapping("/token")
    public ResponseEntity<String> getToken() throws IOException, JSONException {

        final HttpURLConnection conn = getTokenConnection();

        final JSONObject obj = getJsonObject();

        sendRequest(conn, obj);

        final int responseCode = getResponseCode(conn);

        if (responseCode != 200) {
            return ResponseEntity.badRequest().build();
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        final String accessToken = getResponse(br);

        disconnect(conn, br);
        return ResponseEntity.ok(accessToken);
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
