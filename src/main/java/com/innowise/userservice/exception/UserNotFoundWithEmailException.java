package com.innowise.userservice.exception;

public class UserNotFoundWithEmailException extends RuntimeException {
    public UserNotFoundWithEmailException(String email) {
        super("User not found with email: " + email);
    }
}
