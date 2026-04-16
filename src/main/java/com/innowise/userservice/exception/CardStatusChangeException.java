package com.innowise.userservice.exception;

public class CardStatusChangeException extends RuntimeException {
    public CardStatusChangeException(Long cardId) {
        super("Failed to change status for card with id: " + cardId);
    }
}
