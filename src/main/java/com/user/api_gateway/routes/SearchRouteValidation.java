package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.Roles;
import com.user.api_gateway.constants.SearchRoutes;

@Component
public class SearchRouteValidation extends RouteValidation {

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        SearchRoutes searchRoutes = SearchRoutes.matchRoute(uri, httpMethod);

        if (searchRoutes == null)
            return false;

        return Arrays.stream(searchRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
