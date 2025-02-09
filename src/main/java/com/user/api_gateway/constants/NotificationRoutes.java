package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum NotificationRoutes {
    NOTIFICATION_POST("/api/notification", HttpMethod.POST, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NOTIFICATION_PUT("/api/notification", HttpMethod.PUT, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NOTIFICATION_GET("/api/notification", HttpMethod.GET, Roles.values()),
    NOTIFICATION_PATCH("/api/notification", HttpMethod.PATCH, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NOTIFICATION_DELETE("/api/notification/{uuid}", HttpMethod.DELETE, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NOTIFICATION_GET_BY_ID("/api/notification/{uuid}", HttpMethod.GET, Roles.values())
    ;

    String route;
    HttpMethod httpMethod;
    Roles[] rolesAccess;

    private NotificationRoutes(String route, HttpMethod httpMethod, Roles[] rolesAccess) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.rolesAccess = rolesAccess;
    }
    
    // **Matching logic**
    public static NotificationRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(NotificationRoutes.values())
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
