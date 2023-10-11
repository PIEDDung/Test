package com.example.catdog.catdoglovers.exhandler;

public class AccountException extends RuntimeException{
    private static final long serialVersionID = 1;

    public AccountException(String message) {
        super(message);
    }
}
