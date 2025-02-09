package com.user.api_gateway.config;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.*;
import org.springframework.web.servlet.function.RouterFunctions.Builder;

import com.user.api_gateway.constants.BaseRoute;

@Configuration
@AllArgsConstructor
public class Routes {

    final private GatewayRouteConfig url;
    final private JwtRoleFilter jwtRoleFilter;

    private RouterFunction<ServerResponse> createServiceRoutes(String serviceId, Map<String, List<HttpMethod>> routesMethod) {
        Builder builder = GatewayRouterFunctions.route(serviceId);
        routesMethod.forEach((route, methods) -> {
            methods.forEach((method) -> {
                RequestPredicate predicate = RequestPredicates.path(route).and(RequestPredicates.method(method));
                builder.route(predicate, HandlerFunctions.http(url.getServiceUri(serviceId))).filter(jwtRoleFilter);
            });
        });

        return builder.build();
    }

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute(){
        return createServiceRoutes(BaseRoute.AUTHENTICATION.getIdService(), Map.of(
            "/api/auth/sign_in", List.of(HttpMethod.POST),
            "/api/auth/sign_up", List.of(HttpMethod.POST),
            "/api/auth/sign_out", List.of(HttpMethod.POST),
            "/api/auth/validate", List.of(HttpMethod.GET),
            "/api/auth/refresh_token", List.of(HttpMethod.POST),
            "/api/auth/sign_out_all", List.of(HttpMethod.POST),
            "/api/auth/send_reset_password_email", List.of(HttpMethod.POST),
            "/api/auth/reset_password_validated", List.of(HttpMethod.POST),
            "/api/auth/validate_email", List.of(HttpMethod.POST),
            "/api/auth/verified_email", List.of(HttpMethod.POST)
        ));
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes() {
        return createServiceRoutes(BaseRoute.USER.getIdService(), Map.of(
            "/api/user", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH),
            "/api/user/{uuid}", List.of(HttpMethod.GET, HttpMethod.DELETE),
            "/api/roles", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH),
            "/api/roles/{uuid}", List.of(HttpMethod.GET, HttpMethod.DELETE),
            "/api/admin/analytics", List.of(HttpMethod.GET)
        ));
    }
    
    @Bean
    public RouterFunction<ServerResponse> newsServiceRoutes() {
        return createServiceRoutes(BaseRoute.NEWS.getIdService(), Map.of(
            "/api/news", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH),
            "/api/news/{uuid}", List.of(HttpMethod.GET, HttpMethod.DELETE),
            "/api/news/analytics", List.of(HttpMethod.GET)
        ));
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoutes() {
        return createServiceRoutes(BaseRoute.NOTIFICATION.getIdService(), Map.of(
            "/api/notification", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH),
            "/api/notification/{uuid}", List.of(HttpMethod.GET, HttpMethod.DELETE)
        ));
    }

    @Bean
    public RouterFunction<ServerResponse> searchServiceRoutes() {
        return createServiceRoutes("search-service", Map.of(
            "/api/search", List.of(HttpMethod.GET)
        ));
    }

    @Bean
    public RouterFunction<ServerResponse> subscriptionServiceRoutes() {
        return createServiceRoutes("subscription-service", Map.of(
            "/api/subscription", List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE),
            "/api/subscription/{uuid}", List.of(HttpMethod.GET, HttpMethod.DELETE),
            "/api/subscription/user", List.of(HttpMethod.GET),
            "/api/subscription/analytics", List.of(HttpMethod.GET)
        ));
    }
}
