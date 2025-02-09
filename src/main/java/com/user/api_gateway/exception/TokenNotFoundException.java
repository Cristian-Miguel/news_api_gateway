package com.user.api_gateway.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.UNAUTHORIZED;
    }
}
