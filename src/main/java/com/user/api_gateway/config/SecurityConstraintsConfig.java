package com.user.api_gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "gateway.security")
@Data
public class SecurityConstraintsConfig {
    private List<String> whiteList;
    private List<RouteConstraint> roleConstraints;

    @Data
    public static class RouteConstraint {
        private String path;
        private String method;
        private List<String> roles;
    }
}
