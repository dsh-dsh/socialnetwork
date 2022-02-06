package com.skillbox.socialnet.exception;

import java.util.function.Supplier;

public class NoSuchPostException extends RuntimeException {
    public NoSuchPostException() {
    }
    public NoSuchPostException(String message) {
        super(message);
    }

}
