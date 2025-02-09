package com.user.api_gateway.routes;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.Roles;

@Component
public interface RouteFactory {

    public boolean validatedServiceRouteAccess(String uri, HttpMethod httpMethod, Roles role);

}
