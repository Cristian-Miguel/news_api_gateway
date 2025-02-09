package com.user.api_gateway.exception;

import org.springframework.http.HttpStatus;

public class ValidationServiceRouteAccessException extends RuntimeException {
    public ValidationServiceRouteAccessException(String message) {
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.NOT_FOUND;
    }

}
