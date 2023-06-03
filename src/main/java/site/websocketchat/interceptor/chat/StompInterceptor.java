package site.websocketchat.interceptor.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            MessageHeaders messageHeaders = accessor.getMessageHeaders();
            String simpSessionId = (String) messageHeaders.get("simpSessionId");
        }

        if (accessor.getCommand() == StompCommand.CONNECT) {
            MessageHeaders messageHeaders = accessor.getMessageHeaders();
            String simpSessionId = (String) messageHeaders.get("simpSessionId");
        }

        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            MessageHeaders messageHeaders = accessor.getMessageHeaders();
            String simpSessionId = (String) messageHeaders.get("simpSessionId");
        }

        return message;
    }
}
