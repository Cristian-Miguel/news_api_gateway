package com.user.api_gateway.routes;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

@Configuration
@AllArgsConstructor
public class Routes {

    private URLManage url;

    @Bean
    public RouterFunction<ServerResponse> authServiceRoutes(){
        String urlAuth = url.getROUTE_AUTH();
        return GatewayRouterFunctions.route("auth-service")
                .route(RequestPredicates.path("/auth/sign_in"), HandlerFunctions.http(url.getROUTE_AUTH()))
                .route(RequestPredicates.path("/auth/sign_up"), HandlerFunctions.http(url.getROUTE_AUTH()))
                .route(RequestPredicates.path("/auth/sign_out"), HandlerFunctions.http(url.getROUTE_AUTH()))
                .route(RequestPredicates.path("/auth/validate"), HandlerFunctions.http(url.getROUTE_AUTH()))
                .route(RequestPredicates.path("/auth/refresh_token"), HandlerFunctions.http(url.getROUTE_AUTH()))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes(){
        return GatewayRouterFunctions.route("user-service")
                .route(RequestPredicates.path("/user"), HandlerFunctions.http(url.getROUTE_USER()))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> newsServiceRoutes(){
        return GatewayRouterFunctions.route("news-service")
                .route(RequestPredicates.path("/news"), HandlerFunctions.http(url.getROUTE_NEWS()))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoutes(){
        return GatewayRouterFunctions.route("notification-service")
                .route(RequestPredicates.path("/notification"), HandlerFunctions.http(url.getROUTE_NOTIFICATION()))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> searchServiceRoutes(){
        return GatewayRouterFunctions.route("search-service")
                .route(RequestPredicates.path("/search"), HandlerFunctions.http(url.getROUTE_SEARCH()))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> subscriptionServiceRoutes(){
        return GatewayRouterFunctions.route("subscription-service")
                .route(RequestPredicates.path("/subscription"), HandlerFunctions.http(url.getROUTE_SUBSCRIPTION()))
                .build();
    }
}
