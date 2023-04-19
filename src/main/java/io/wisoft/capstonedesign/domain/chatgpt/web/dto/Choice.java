package io.wisoft.capstonedesign.domain.chatgpt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(
        String text,
        Integer index,
        @JsonProperty("finish_reason")
        String finishReason) {
}
