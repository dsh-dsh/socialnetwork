package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.model.RS.DefaultRS;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchUserException.class)
    protected ResponseEntity<?> handleNoSuchUserException(NoSuchUserException ex) {
        DefaultRS<String> defaultRS = new DefaultRS<>();
        defaultRS.setError(ex.getMessage());
        return new ResponseEntity<>(defaultRS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    protected ResponseEntity<?> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        DefaultRS<String> defaultRS = new DefaultRS<>();
        defaultRS.setError(ex.getMessage());
        return new ResponseEntity<>(defaultRS, HttpStatus.FORBIDDEN);
    }

}
