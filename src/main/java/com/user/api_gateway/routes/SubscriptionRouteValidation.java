package com.user.api_gateway.routes;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.Roles;
import com.user.api_gateway.constants.SubscriptionRoutes;

@Component
public class SubscriptionRouteValidation extends RouteValidation{

    @Override
    protected boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role) {
        SubscriptionRoutes subscriptionRoutes = SubscriptionRoutes.matchRoute(uri, httpMethod);

        if (subscriptionRoutes == null)
            return false;

        return Arrays.stream(subscriptionRoutes.getRolesAccess())
            .anyMatch(roleEnum -> roleEnum.equals(role));
    }

}
