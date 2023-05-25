package io.wisoft.capstonedesign.domain.chatgpt.application;

import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptRequest;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.global.config.ChatGptConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChatGptServiceImplTest {

    @Autowired
    ChatGptServiceImpl chatGptService;

    @Test
    public void askQuestion() throws Exception {
        final String MESSAGE = "안녕";

        //given -- 조건
        //when -- 동작

        final ChatGptResponse response = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                MESSAGE,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P)));

        //then -- 검증
        Assertions.assertThat(response).isNotNull();

    }

    //comment: Build headers
    private HttpEntity<ChatGptRequest> buildHttpEntity(final ChatGptRequest chatGptRequest) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        return new HttpEntity<>(chatGptRequest, headers);
    }

    //comment: Generate response
    private ChatGptResponse getResponse(final HttpEntity<ChatGptRequest> chatGptRequestHttpEntity) {
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestHttpEntity,
                ChatGptResponse.class);

        return responseEntity.getBody();
    }
}