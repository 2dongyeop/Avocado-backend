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

import static io.wisoft.capstonedesign.global.config.ChatGptConfig.MEDIA_TYPE;

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
        headers.setContentType(MediaType.parseMediaType(MEDIA_TYPE));
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
            case "뼈가 부었어요" ->
                    "주변 정형외과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 정형외과는 아래 3곳이 있습니다. 1. 대전센텀병원 2. 마라톤정형외과병원 3. S&K병원";
            case "이가 시려요" ->
                    "주변 치과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 치과는 아래 3곳이 있습니다. 1. 서울브라운치과병원 2. 세상에하나뿐인치과의원 3. 연세고운미소치과의원";
            case "코를 높이고 싶어요" ->
                    "주변 성형외과를 방문해보시길 바랍니다. 대전을 기준으로 리뷰가 많은 성형외과는 아래 3곳이 있습니다. 1. 페이스성형외과의원 2. 양승돈성형외과의원 3. 닥터스미성형외과의원";
            case "가슴통증" -> "순환기내과";
            case "골다공증", "발목접질림", "방사통", "손목통증" -> "정형외과";
            case "관절통증" -> "류마티스내과";
            case "기억장애・치매", "불면증・기면증", "손・발 저림" -> "신경과";
            case "목・허리통증", "삼킴곤란・사래걸림" -> "재활의학과";
            case "다리부종" -> "혈관이식외과";
            case "담석" -> "간담췌외과";
            case "만성통증" -> "마취통증의학과";
            case "목 종괴" -> "갑상선외과";
            case "목 이물감", "코골이・무호흡" -> "이비인후과";
            case "미숙아성장부진", "소아천식・아토피", "잦은 감염・발육부진" -> "소아청소년과";
            case "복통・설사・혈변", "복부통증" -> "소화기내과";
            case "배뇨장애・혈변" -> "비뇨의학과";
            case "비만" -> "위장관외과";
            case "시력저하" -> "안과";
            case "손발톱변형", "안면홍조" -> "피부과";
            case "안면통증・안면종괴" -> "성형외과";
            case "옆구리통증・혈뇨・거품뇨" -> "신장내과";
            case "월경통・질출혈" -> "산부인과";
            case "유방함・유방 종괴" -> "유방외과";
            case "급격한 체중변화" -> "내분비내과";
            case "학습장애・집중력저하" -> "정신건강의학과";
            default -> "질문 게시판을 활용해 보세요.";
        };
    }
}
