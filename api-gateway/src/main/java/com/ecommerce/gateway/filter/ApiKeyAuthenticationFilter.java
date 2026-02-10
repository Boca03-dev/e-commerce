package com.ecommerce.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
    
    private static final String API_KEY_HEADER = "X-API-Key";
    
    private static final List<String> VALID_API_KEYS = Arrays.asList(
        "ecommerce-secret-key-123",
        "admin-key-456",
        "test-key-789"
    );
    
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/actuator/health",
        "/actuator/info",
        "/eureka"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        
        if (isPublicPath(path)) {
            logger.debug("Public path accessed: {}", path);
            return chain.filter(exchange);
        }
        
        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("🔒 API Key missing for path: {}", path);
            return unauthorized(exchange, "API Key is required");
        }
        
        if (!isValidApiKey(apiKey)) {
            logger.warn("🔒 Invalid API Key attempt for path: {}", path);
            return unauthorized(exchange, "Invalid API Key");
        }
        
        logger.info("✅ Valid API Key for path: {}", path);
        return chain.filter(exchange);
    }
    
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
    
    private boolean isValidApiKey(String apiKey) {
        return VALID_API_KEYS.contains(apiKey);
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        
        String errorJson = String.format(
            "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
            message,
            java.time.Instant.now().toString()
        );
        
        byte[] bytes = errorJson.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(
            Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }

    @Override
    public int getOrder() {
        return -100; 
    }
}
