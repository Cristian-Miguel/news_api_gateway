package com.user.api_gateway.constants;

import java.util.Arrays;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public enum AuthenticationRoutes {
    SIGN_UP("/api/auth/sign_up", Roles.values()),
    SIGN_IN("/api/auth/sign_in", Roles.values()),
    SIGN_OUT("/api/auth/sign_out", new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    SIGN_OUT_ALL("/apiauth/sign_out_all", new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    VALIDATE("/api/auth/validate", Roles.values()),
    REFRESH_TOKEN("/api/auth/refresh_token", Roles.values()),
    SEND_RESET_PASSWORD_EMAIL("/api/auth/send_reset_password_email", Roles.values()),
    RESET_PASSWORD_VALIDATED("/api/auth/reset_password_validated", new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    VALIDATE_EMAIL("/api/auth/validate_email", new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE}),
    VERIFIED_EMAIL("/api/auth/verified_email", new Roles[]{Roles.ADMINISTRATOR, Roles.PUBLISHER, Roles.JOURNALIST, Roles.READER, Roles.PREMIUM, Roles.NEWS_ENTERPRICE})
    ;

    String route;
    Roles[] rolesAccess;

    private AuthenticationRoutes( String route, Roles[] rolesAccess ){
        this.route = route;
        this.rolesAccess = rolesAccess;
    }

    // **Matching logic**
    public static AuthenticationRoutes matchRoute(String path, HttpMethod method) {
        return Arrays.stream(AuthenticationRoutes.values())
                .filter(routeEnum -> routeEnum.getRoute().equals(path))
                .findFirst()
                .orElse(null);
    }
}
