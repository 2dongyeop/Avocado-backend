package io.wisoft.capstonedesign.domain.chatgpt.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.chatgpt.application.ChatGptService;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatRequest;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Tag(name = "메인화면 검색")
@RestController
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;
    @Qualifier("asyncExecutor") private final ThreadPoolTaskExecutor executor;

    @SwaggerApi(summary = "OpenAI를 이용한 메인화면 검색", implementation = ChatGptResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/search")
    public ChatGptResponse sendMessage(@RequestBody final ChatRequest chatRequest) {
        final CompletableFuture<ChatGptResponse> future = CompletableFuture.supplyAsync(
                () -> chatGptService.askQuestion(chatRequest), executor)
                .orTimeout(5, TimeUnit.SECONDS);

        return future.join();
    }
}
