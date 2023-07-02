package io.wisoft.capstonedesign.global.config;


import org.springframework.beans.factory.annotation.Value;

public class ChatGptConfig {


    @Value(value = "${chatgpt.authorization}")
    public static String AUTHORIZATION;

    @Value(value = "${chatgpt.token-type}")
    public static String TOKEN_TYPE;

    @Value(value = "${chatgpt.api-key}")
    public static String API_KEY;

    @Value(value = "${chatgpt.model}")
    public static String MODEL;

    @Value(value = "${chatgpt.max-token}")
    public static Integer MAX_TOKEN;

    @Value(value = "${chatgpt.temperature}")
    public static Double TEMPERATURE;

    @Value(value = "${chatgpt.top-p}")
    public static Double TOP_P;

    @Value(value = "${chatgpt.media-type}")
    public static String MEDIA_TYPE;

    @Value(value = "${chatgpt.url}")
    public static String URL;
}

