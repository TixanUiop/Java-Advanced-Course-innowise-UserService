package com.innowise.userservice.exception;

public class StatusChangeErrorException extends RuntimeException {
    public StatusChangeErrorException() {
        super("Status change error");
    }
}
