package com.evgeny.innowisetasks.Exception;

public class StatusChangeErrorException extends RuntimeException {
    public StatusChangeErrorException() {
        super("Status change error");
    }
}
