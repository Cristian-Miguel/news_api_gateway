package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum SubscriptionRoutes {
    SUBSCRIPTION_POST("/api/susbcription", HttpMethod.POST, new Roles[]{Roles.ADMINISTRATOR}),
    SUBSCRIPTION_PUT("/api/susbcription", HttpMethod.PUT, new Roles[]{Roles.ADMINISTRATOR}),
    SUBSCRIPTION_GET("/api/susbcription", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    SUBSCRIPTION_PATCH("/api/susbcription", HttpMethod.PATCH, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    SUBSCRIPTION_DELETE("/api/susbcription/{uuid}", HttpMethod.DELETE, new Roles[]{Roles.ADMINISTRATOR}),
    SUBSCRIPTION_GET_BY_ID("/api/susbcription/{uuid}", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    SUBSCRIPTION_GET_USERS("/api/susbcription/user/", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    SUBSCRIPTION_GET_USER_ID("/api/susbcription/user/{uuid}", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE, Roles.PREMIUM, Roles.READER}),
    ;

    String route;
    HttpMethod httpMethod;
    Roles[] rolesAccess;

    private SubscriptionRoutes(String route, HttpMethod httpMethod, Roles[] rolesAccess) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.rolesAccess = rolesAccess;
    }
    
    // **Matching logic**
    public static SubscriptionRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(SubscriptionRoutes.values())
                .filter(routeEnum -> matchPath(path, routeEnum.getRoute()) && routeEnum.getHttpMethod().equals(method))
                .findFirst()
                .orElse(null);
    }

    // **Check if path matches, handling UUIDs**
    private static boolean matchPath(String requestPath, String enumPath) {
        String regex = enumPath.replace("{uuid}", "[a-f0-9\\-]{36}");
        return requestPath.matches(regex);
    }
}
