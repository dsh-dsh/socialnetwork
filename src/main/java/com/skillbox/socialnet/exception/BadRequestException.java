package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.util.Constants;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super(Constants.BAD_REQUEST_MESSAGE);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
