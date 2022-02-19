package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.util.Constants;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException() {
        super(Constants.NO_SUCH_USER_MESSAGE);
    }
    public NoSuchUserException(String message) {
        super(message);
    }
}
