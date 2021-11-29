package com.skillbox.socialnet.exception;

public class NoAnyPostsFoundException extends RuntimeException{
    public NoAnyPostsFoundException() {
    }

    public NoAnyPostsFoundException(String message) {
        super(message);
    }
}
