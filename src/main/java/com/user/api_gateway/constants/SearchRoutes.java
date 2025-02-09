package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum SearchRoutes {
    SEARCH_GET("/api/search", HttpMethod.GET, Roles.values()),
    SEARCH_GET_BY_ID("/api/search/{uuid}", HttpMethod.GET, Roles.values())
    ;

    String route;
    HttpMethod httpMethod;
    Roles[] rolesAccess;

    private SearchRoutes(String route, HttpMethod httpMethod, Roles[] rolesAccess) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.rolesAccess = rolesAccess;
    }
    
    // **Matching logic**
    public static SearchRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(SearchRoutes.values())
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
