package com.chat.project.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class SpringConfig implements WebSocketMessageBrokerConfigurer {

    /***
     * 엔드포인트 -> 일종의 통신의 도착 지점.
     * 즉 특정한 통신이 어떤 엔드포인트에 도착했을 때 어떤 행위를 하게 만들 것이다.
     * 아래 /ws-stomp로 하면, 웹 소켓 통신이 해당 엔드 포인트에 도착했을때
     * 우리는 해당 통신이 웹 소켓 통신 중 stomp인것을 확인하고, 연결한다는 의미다.
     *
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 주소 url => /ws-stomp
        registry.addEndpoint("/ws-stomp") // 연결될 엔드포인트
                .withSockJS(); // SocketJS 를 연결한다는 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
        registry.enableSimpleBroker("/sub");

        // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }


}
