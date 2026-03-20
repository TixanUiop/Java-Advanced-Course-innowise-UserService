package com.evgeny.innowisetasks.Exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Card not found with id: " + id);
    }
}
