package io.wisoft.capstonedesign.domain.chatgpt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatGptRequest(
        String model,
        String prompt,
        @JsonProperty("max_tokens") Integer maxTokens,
        Double temperature,
        @JsonProperty("top_p") Double topP) { }
