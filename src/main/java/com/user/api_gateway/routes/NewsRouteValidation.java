package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.NewsRoutes;
import com.user.api_gateway.constants.Roles;

@Component
public class NewsRouteValidation extends RouteValidation {

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        NewsRoutes newsRoutes = NewsRoutes.matchRoute(uri, httpMethod);

        if (newsRoutes == null)
            return false;

        return Arrays.stream(newsRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
