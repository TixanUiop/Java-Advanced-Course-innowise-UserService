package com.innowise.innowisetasks.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User " + email + " already exists");
    }
}
