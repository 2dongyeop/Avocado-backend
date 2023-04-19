package io.wisoft.capstonedesign.domain.chatgpt.web;

import io.wisoft.capstonedesign.domain.chatgpt.application.ChatGptService;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping("/api/search")
    public ChatGptResponse sendMessage(@RequestBody final ChatRequest chatRequest) {
        return chatGptService.askQuestion(chatRequest);
    }
}
