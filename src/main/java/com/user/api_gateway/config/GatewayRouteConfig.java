package com.user.api_gateway.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.BaseRoute;

@Component
@ConfigurationProperties(prefix = "spring.cloud.gateway")
public class GatewayRouteConfig {

    @Value("${microservice.auth_service}")
    private String authRoute;

    @Value("${microservice.user_service}")
    private String userRoute;

    @Value("${microservice.news_service}")
    private String newsRoute;

    @Value("${microservice.notification_service}")
    private String notificationRoute;

    @Value("${microservice.search_service}")
    private String searchRoute;

    @Value("${microservice.subscription_service}")
    private String susbcriptionRoute;

    public String getServiceUri(String serviceId) {
        return mapRoutesById().get(serviceId);
    }

    private Map<String, String> mapRoutesById(){
        return Map.of(
            BaseRoute.AUTHENTICATION.getIdService(), authRoute,
            BaseRoute.NEWS.getIdService(), newsRoute,
            BaseRoute.NOTIFICATION.getIdService(), notificationRoute,
            BaseRoute.SEARCH.getIdService(), searchRoute,
            BaseRoute.SUBSCRIPTION.getIdService(), susbcriptionRoute,
            BaseRoute.USER.getIdService(), userRoute
        );
    }
}
