package com.user.api_gateway.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.api_gateway.constants.Roles;
import com.user.api_gateway.exception.TokenNotFoundException;
import com.user.api_gateway.exception.ValidationServiceRouteAccessException;
import com.user.api_gateway.models.GenericErrorResponse;
import com.user.api_gateway.routes.RouteFactory;
import com.user.api_gateway.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.io.IOException;

import org.apache.hc.core5.http.HttpHeaders;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtRoleFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    final private JwtUtils jwtUtils;
    final private RouteFactory routeFactory;

    @SuppressWarnings("null")
    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String uri = request.path();
        HttpMethod httpMethod = request.method();
        //Change this, some routes can has public access
        String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return routeFactory.validatedServiceRouteAccess(uri, httpMethod, Roles.PUBLIC) ?
                next.handle(request):
                handleErrorResponse(request, "Forbidden: Insufficient role permissions.", HttpStatus.FORBIDDEN, uri);
        }

        if (!authHeader.startsWith("Bearer "))
            throw new TokenNotFoundException("Invalid token in the header") ;

        try {
            String token = authHeader.substring(7);

            Claims claims = jwtUtils.getAllClaims(token);

            String roleName = claims.get("role", String.class);
            if (roleName == null)
                return handleErrorResponse(request, "User has no assigned roles.", HttpStatus.UNAUTHORIZED, uri);

            Roles role = Roles.matchRoleByName(roleName);

            if(role == null)
                return handleErrorResponse(request, "Forbidden: Insufficient role permissions.", HttpStatus.FORBIDDEN, uri);

            if(!routeFactory.validatedServiceRouteAccess(uri, httpMethod, role))
                return handleErrorResponse(request, "Forbidden: Insufficient role permissions.", HttpStatus.FORBIDDEN, uri);

            ServerRequest internalRequest = ServerRequest.from(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.getToken(
                    roleName, 
                    claims.get("email", String.class), 
                    claims.get("uuid", String.class), 
                    claims.getSubject()
                )
            ).build();

            return next.handle(internalRequest);
        } catch (ExpiredJwtException ex){
            return handleErrorResponse(request, "Token expired.", HttpStatus.UNAUTHORIZED, uri);
        } catch (MalformedJwtException ex){
            return handleErrorResponse(request, "Token malformed.", HttpStatus.UNAUTHORIZED, uri);
        } catch (TokenNotFoundException ex){
            return handleErrorResponse(request, ex.getMessage(), ex.getStatus(), uri);
        } catch (ValidationServiceRouteAccessException ex){
            return handleErrorResponse(request, ex.getMessage(), ex.getStatus(), uri);
        } catch (Exception ex){
            return handleErrorResponse(request, "Invalid token.", HttpStatus.UNAUTHORIZED, uri);
        }
    }

    private ServerResponse handleErrorResponse(
            ServerRequest request,
            String message, HttpStatus httpStatus, String uri
    ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse =
        objectMapper.writeValueAsString(
                GenericErrorResponse.builder()
                        .timestamp(null)
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(message)
                        .path(uri)
                        .build()
        );
        
        return ServerResponse.status(httpStatus).body(jsonResponse);
    }
}
