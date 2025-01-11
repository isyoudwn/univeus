package com.example.univeus.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        // 인메모리 메시지 브로커 사용한다 -> 이후에 메시지 브로커 서버 분리.

        // 클라이언트가 메시지 받을 경로
        messageBrokerRegistry.enableSimpleBroker("/subscribe");

        // 클라이언트가 메시지 보낼 경로
        messageBrokerRegistry.setApplicationDestinationPrefixes("/publish");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 모든 출처 허용
                .withSockJS();
    }
}
