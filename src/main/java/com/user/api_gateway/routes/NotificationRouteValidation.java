package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.NotificationRoutes;
import com.user.api_gateway.constants.Roles;

@Component
public class NotificationRouteValidation extends RouteValidation {

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        NotificationRoutes notificationRoutes = NotificationRoutes.matchRoute(uri, httpMethod);

        if (notificationRoutes == null)
            return false;

        return Arrays.stream(notificationRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
