package com.innowise.userservice.exception;

public class InvalidOrExpiredToken extends RuntimeException {
    public InvalidOrExpiredToken() {
        super("Invalid or expired token");
    }
}
