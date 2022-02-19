package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.util.Constants;

public class NoSuchPostException extends RuntimeException {
    public NoSuchPostException() {
        super(Constants.NO_SUCH_POST_MESSAGE);
    }
    public NoSuchPostException(String message) {
        super(message);
    }

}
