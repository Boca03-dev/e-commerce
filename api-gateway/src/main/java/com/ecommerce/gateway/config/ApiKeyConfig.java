package com.ecommerce.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api.security")
public class ApiKeyConfig {

    private List<String> validKeys = new ArrayList<>();
    private List<String> publicPaths = new ArrayList<>();
    
    public List<String> getValidKeys() {
        return validKeys;
    }

    public void setValidKeys(List<String> validKeys) {
        this.validKeys = validKeys;
    }

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
