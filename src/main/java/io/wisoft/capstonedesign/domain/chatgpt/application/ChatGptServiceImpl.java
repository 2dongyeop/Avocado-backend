package io.wisoft.capstonedesign.domain.chatgpt.application;

import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptRequest;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatRequest;
import io.wisoft.capstonedesign.global.config.ChatGptConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ChatGptServiceImpl implements ChatGptService {
    private static final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ChatGptResponse askQuestion(final ChatRequest chatRequest) {
        final ChatGptResponse response = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                chatRequest.message(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P)));

        log.info("OpenAI 요청이 들어왔습니다 : {} ", chatRequest.message());
        log.info("답변 : {}", response.choices().get(0).text());
        return response;
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
        final ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestHttpEntity,
                ChatGptResponse.class);

        return responseEntity.getBody();
    }
}
