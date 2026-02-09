package com.ecommerce.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket konfiguracija za real-time notifikacije
 * Koristi STOMP protokol preko WebSocket-a
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Konfiguriše message broker
     * /topic - za broadcast poruke svim klijentima
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker za destination prefixes
        config.enableSimpleBroker("/topic");
        
        // Prefix za poruke koje dolaze od klijenata
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registruje STOMP endpoint
     * Klijenti se povezuju na ws://localhost:8083/ws
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Dozvoljava sve origine (za dev)
                .withSockJS();  // Fallback na SockJS ako WebSocket nije dostupan
    }
}
