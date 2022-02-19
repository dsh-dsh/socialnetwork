package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.util.Constants;

public class NoAnyPostsFoundException extends RuntimeException{
    public NoAnyPostsFoundException() {
        super(Constants.NO_ANY_POST_MESSAGE);
    }
    public NoAnyPostsFoundException(String message) {
        super(message);
    }
}
