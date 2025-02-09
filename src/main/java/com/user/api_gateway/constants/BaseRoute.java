package com.user.api_gateway.constants;

import lombok.Getter;

@Getter
public enum BaseRoute {

    AUTHENTICATION("auth", "auth-service"),
    USER("user", "user-service"),
    NEWS("news", "news-service"),
    NOTIFICATION("notification", "notification-service"),
    SEARCH("search", "search-service"),
    SUBSCRIPTION("subscription", "subscription-service")
    ;
    
    String baseRoute;
    String idService;

    private BaseRoute( String baseRoute, String idService ){
        this.baseRoute = baseRoute;
        this.idService = idService;
    }

}
