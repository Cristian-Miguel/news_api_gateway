package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum UserRoutes {
    USER_POST("/api/user", HttpMethod.POST, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    USER_PUT("/api/user", HttpMethod.PUT, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    USER_GET("/api/user", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    USER_PATCH("/api/user", HttpMethod.PATCH, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    USER_DELETE("/api/user/{uuid}", HttpMethod.DELETE, new Roles[]{Roles.ADMINISTRATOR, Roles.NEWS_ENTERPRICE}),
    USER_GET_BY_ID("/api/user/{uuid}", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    ROLE_POST("/api/roles", HttpMethod.POST, new Roles[]{Roles.ADMINISTRATOR}),
    ROLE_PUT("/api/roles", HttpMethod.PUT, new Roles[]{Roles.ADMINISTRATOR}),
    ROLE_GET("/api/roles", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR}),
    ROLE_PATCH("/api/roles", HttpMethod.PATCH, new Roles[]{Roles.ADMINISTRATOR}),
    ROLE_DELETE("/api/roles/{uuid}", HttpMethod.DELETE, new Roles[]{Roles.ADMINISTRATOR}),
    ROLE_GET_BY_ID("/api/roles/{uuid}", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR}),
    ADMIN_ANALYTICS("/api/admin/analytics", HttpMethod.GET, new Roles[]{Roles.ADMINISTRATOR}),
    ;

    String route;
    HttpMethod httpMethod;
    Roles[] rolesAccess;

    private UserRoutes(String route, HttpMethod httpMethod, Roles[] rolesAccess) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.rolesAccess = rolesAccess;
    }
    
    // **Matching logic**
    public static UserRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(UserRoutes.values())
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
