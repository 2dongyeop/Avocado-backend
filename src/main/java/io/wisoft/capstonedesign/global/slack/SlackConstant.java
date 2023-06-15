package io.wisoft.capstonedesign.global.slack;


public enum SlackConstant {

    //slack 채널명
    ERROR_CHANNEL("#장애확인"),
    TALKING_CHANNEL("#잡담"),
    ANNOUNCEMENT_CHANNEL("#공지");

    private String channel;

    SlackConstant(final String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
