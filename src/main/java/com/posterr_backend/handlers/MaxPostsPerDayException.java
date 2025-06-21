package com.posterr_backend.handlers;

public class MaxPostsPerDayException extends RuntimeException {
    public MaxPostsPerDayException(String message) {
        super(message);
    }
}
