package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.AuthenticationRoutes;
import com.user.api_gateway.constants.Roles;

@Component
@Primary
public class AuthenticationRouteValidation extends RouteValidation {

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        AuthenticationRoutes authenticationRoutes = AuthenticationRoutes.matchRoute(uri, httpMethod);

        if (authenticationRoutes == null)
            return false;

        return Arrays.stream(authenticationRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
