package io.wisoft.capstonedesign.domain.chatgpt.web.dto;

import java.time.LocalDate;
import java.util.List;

public record ChatGptResponse(
        String id,
        String object,
        LocalDate created,
        String model,
        List<Choice>choices
) { }
