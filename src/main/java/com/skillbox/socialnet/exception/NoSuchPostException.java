package com.skillbox.socialnet.exception;

public class NoSuchPostException extends RuntimeException {
    public NoSuchPostException() {
    }
    public NoSuchPostException(String message) {
        super(message);
    }

}
