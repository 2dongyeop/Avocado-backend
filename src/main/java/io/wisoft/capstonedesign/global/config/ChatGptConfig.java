package io.wisoft.capstonedesign.global.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Configuration
@Component
public class ChatGptConfig {
    //static 없애고 getter 만들기?

    @Value(value = "${chatgpt.authorization}")
    public String AUTHORIZATION;

    @Value(value = "${chatgpt.token-type}")
    public String TOKEN_TYPE;

    @Value(value = "${chatgpt.api-key}")
    public String API_KEY;

    @Value(value = "${chatgpt.model}")
    public String MODEL;

    @Value(value = "${chatgpt.max-token}")
    public Integer MAX_TOKEN;

    @Value(value = "${chatgpt.temperature}")
    public Double TEMPERATURE;

    @Value(value = "${chatgpt.top-p}")
    public Double TOP_P;

    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    @Value(value = "${chatgpt.url}")
    public String URL;
}

