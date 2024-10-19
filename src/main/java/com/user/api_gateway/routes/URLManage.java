package com.user.api_gateway.routes;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class URLManage {

    @Value("${microservice.auth_service}")
    private String ROUTE_AUTH;

    @Value("${microservice.user_service}")
    private String ROUTE_USER;

    @Value("${microservice.news_service}")
    private String ROUTE_NEWS;

    @Value("${microservice.notification_service}")
    private String ROUTE_NOTIFICATION;

    @Value("${microservice.search_service}")
    private String ROUTE_SEARCH;

    @Value("${microservice.subscription_service}")
    private String ROUTE_SUBSCRIPTION;

}
