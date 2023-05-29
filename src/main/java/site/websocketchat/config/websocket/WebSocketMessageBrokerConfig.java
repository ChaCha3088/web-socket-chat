package site.websocketchat.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp 접속 주소 url 설정
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("https://localhost:8080")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메시지를 구독하는 요청 url => 메시지 받을 때
        registry.enableSimpleBroker("/sub");

        //메시지를 발행하는 요청 url => 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
