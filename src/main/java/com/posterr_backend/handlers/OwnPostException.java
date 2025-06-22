package com.posterr_backend.handlers;

public class OwnPostException extends RuntimeException {
    public OwnPostException(String message) {
        super(message);
    }
}