package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.Roles;
import com.user.api_gateway.constants.UserRoutes;

@Component
public class UserRouteValidation extends RouteValidation {

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        UserRoutes userRoutes = UserRoutes.matchRoute(uri, httpMethod);

        if (userRoutes == null)
            return false;

        return Arrays.stream(userRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
