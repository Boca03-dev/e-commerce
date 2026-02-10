package com.ecommerce.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    
    private static final String API_KEY_HEADER = "X-API-Key";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();
        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);
        
        logger.info("========================================");
        logger.info("Gateway Request: {} {}", method, path);
        
        if (apiKey != null && !apiKey.isEmpty()) {
            String maskedKey = apiKey.substring(0, Math.min(10, apiKey.length())) + "...";
            logger.info("API Key: {}", maskedKey);
        } else {
            logger.info("API Key: NOT PROVIDED");
        }
        
        logger.info("========================================");
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Gateway Response Status: {}", 
                       exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return -99; 
    }
}
