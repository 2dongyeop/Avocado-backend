package io.wisoft.capstonedesign.domain.chatgpt.application;

import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptRequest;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponseV2;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatRequest;
import io.wisoft.capstonedesign.global.config.ChatGptConfig;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final ChatGptConfig ChatGptConfig;

    public ChatGptServiceImpl(ChatGptConfig chatGptConfig) {
        this.ChatGptConfig = chatGptConfig;
    }


    @Override
    public ChatGptResponse askQuestion(final ChatRequest chatRequest) {
        final ChatGptResponse response = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                chatRequest.symptom(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P)));

        log.info("OpenAI 요청이 들어왔습니다 : {} ", chatRequest.symptom());
        log.info("답변 : {}", response.choices().get(0).text());
        return response;
    }

    @Override
    public ChatGptResponseV2 askQuestionV2(final ChatRequest chatRequest) {
        return new ChatGptResponseV2(getMessage(chatRequest));
    }


    //comment: Build headers

    private HttpEntity<ChatGptRequest> buildHttpEntity(final ChatGptRequest chatGptRequest) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.TOKEN_TYPE + ChatGptConfig.API_KEY);
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

    @NotNull
    private String getMessage(final ChatRequest chatRequest) {
        return switch (chatRequest.symptom()) {

            case "뼈가 부었어요" -> {
                yield "주변 정형외과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 정형외과는 아래 3곳이 있습니다. 1. 대전센텀병원 2. 마라톤정형외과병원 3. S&K병원";
            }
            case "이가 시려요" -> {
                yield "주변 치과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 치과는 아래 3곳이 있습니다. 1. 서울브라운치과병원 2. 세상에하나뿐인치과의원 3. 연세고운미소치과의원";
            }
            case "코를 높이고 싶어요" -> {
                yield "주변 성형외과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 성형외과는 아래 3곳이 있습니다. 1. 페이스성형외과의원 2. 양승돈성형외과의원 3. 닥터스미성형외과의원";
            }
            default -> {
                yield "다시 질문해주세요.";
            }
        };
    }
}
