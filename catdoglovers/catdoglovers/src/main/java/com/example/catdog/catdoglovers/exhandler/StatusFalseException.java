package com.example.catdog.catdoglovers.exhandler;

import javax.naming.AuthenticationException;

public class StatusFalseException extends RuntimeException {
    public StatusFalseException(String message) {
        super(message);
    }
}
