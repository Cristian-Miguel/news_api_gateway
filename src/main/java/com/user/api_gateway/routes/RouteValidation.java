package com.user.api_gateway.routes;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.user.api_gateway.constants.BaseRoute;
import com.user.api_gateway.constants.Roles;
import com.user.api_gateway.exception.ValidationServiceRouteAccessException;

@Component
public abstract class RouteValidation implements RouteFactory {

    @Override
    public boolean validatedServiceRouteAccess(String uri, HttpMethod httpMethod, Roles role) 
    throws ValidationServiceRouteAccessException{
        for (Map.Entry<BaseRoute, RouteValidation> entry : getClassToChoose().entrySet())
            if( uri.contains(entry.getKey().getBaseRoute()) )
                return entry.getValue().validateRouteByRole(uri, httpMethod, role);
        
        throw new ValidationServiceRouteAccessException("The "+uri+" wasn't found in the system.");
    }

    protected abstract boolean validateRouteByRole(String uri, HttpMethod httpMethod, Roles role);

    private Map<BaseRoute, RouteValidation> getClassToChoose() {
        return Map.of(
            BaseRoute.AUTHENTICATION, new AuthenticationRouteValidation(),
            BaseRoute.USER, new UserRouteValidation(),
            BaseRoute.NEWS, new NewsRouteValidation(),
            BaseRoute.NOTIFICATION, new NotificationRouteValidation(),
            BaseRoute.SEARCH, new SearchRouteValidation(),
            BaseRoute.SUBSCRIPTION, new SubscriptionRouteValidation()
        );
    }
}
