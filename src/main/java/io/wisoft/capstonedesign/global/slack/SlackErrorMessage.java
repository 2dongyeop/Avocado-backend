package io.wisoft.capstonedesign.global.slack;

import java.time.LocalDateTime;

public record SlackErrorMessage(LocalDateTime dateTime, String message) {

    @Override
    public String toString() {
        return  "장애 발생! 🔥" +
                "\n에러 발생 일시 = " + dateTime +
                "\n에러 메시지 = " + message;
    }
}
