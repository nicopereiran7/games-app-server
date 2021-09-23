package com.games.web.app.exceptions;

public class RolNotFoundException extends RuntimeException{
    public RolNotFoundException(String message) {
        super(message);
    }
}
