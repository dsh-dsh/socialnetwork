package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.util.Constants;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException() {
        super(Constants.BAD_REQUEST_MESSAGE);
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
