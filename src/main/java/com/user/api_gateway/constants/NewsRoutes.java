package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum NewsRoutes {
    NEWS_POST("/api/news", HttpMethod.POST, new Roles[]{Roles.ADMINISTRATOR, Roles.JOURNALIST, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NEWS_PUT("/api/news", HttpMethod.PUT, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NEWS_GET("/api/news", HttpMethod.GET, Roles.values()),
    NEWS_PATCH("/api/news", HttpMethod.PATCH, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NEWS_DELETE("/api/news/{uuid}", HttpMethod.DELETE, new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.NEWS_ENTERPRICE}),
    NEWS_GET_BY_ID("/api/news/{uuid}", HttpMethod.GET, Roles.values()),
    ;

    String route;
    HttpMethod httpMethod;
    Roles[] rolesAccess;

    private NewsRoutes(String route, HttpMethod httpMethod, Roles[] rolesAccess) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.rolesAccess = rolesAccess;
    }

    // **Matching logic**
    public static NewsRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(NewsRoutes.values())
                .filter(newsEnum -> matchPath(path, newsEnum.getRoute()) && newsEnum.getHttpMethod().equals(method))
                .findFirst()
                .orElse(null);
    }

    // **Check if path matches, handling UUIDs**
    private static boolean matchPath(String requestPath, String enumPath) {
        String regex = enumPath.replace("{uuid}", "[a-f0-9\\-]{36}");
        return requestPath.matches(regex);
    }
}
