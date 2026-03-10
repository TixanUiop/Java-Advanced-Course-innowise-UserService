package com.evgeny.innowisetasks.Exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User " + email + " already exists");
    }
}
