package com.example.demo.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingInterceptor implements ChannelInterceptor {

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("Message sent to channel: " + channel + " with payload: " + message.getPayload());
    }
}
