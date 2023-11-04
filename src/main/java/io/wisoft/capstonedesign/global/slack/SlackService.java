package io.wisoft.capstonedesign.global.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackService {

    @Value(value = "${slack.is-off}")
    private String slackOff;

    @Value(value = "${slack.token}")
    private String slackToken;

    public void sendSlackMessage(final SlackErrorMessage message, final SlackConstant slackConstant) {

        if (Boolean.parseBoolean(slackOff)) {
            return;
        }

        final String channel = slackConstant.getChannel();

        try {
            final MethodsClient slackBot = Slack.getInstance().methods(slackToken);

            final ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message.toString())
                    .build();

            slackBot.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
