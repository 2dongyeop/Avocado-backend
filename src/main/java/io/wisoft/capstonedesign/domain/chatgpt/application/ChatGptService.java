package io.wisoft.capstonedesign.domain.chatgpt.application;

import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponse;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatGptResponseV2;
import io.wisoft.capstonedesign.domain.chatgpt.web.dto.ChatRequest;

public interface ChatGptService {
    ChatGptResponse askQuestion(final ChatRequest chatRequest);
    ChatGptResponseV2 askQuestionV2(final ChatRequest chatRequest);
}
