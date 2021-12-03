package com.skillbox.socialnet.exception;

import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    // TODO прописать правильные сообщения об ошибках в Константах и возвращать правильные HttpStatus

    @ExceptionHandler(NoSuchUserException.class)
    protected ResponseEntity<?> handleNoSuchUserException(NoSuchUserException ex) {
        return new ResponseEntity<>(DefaultRSMapper.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    protected ResponseEntity<?> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        return new ResponseEntity<>(DefaultRSMapper.error(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoAnyPostsFoundException.class)
    protected ResponseEntity<?> handleNoSuchUserException(NoAnyPostsFoundException ex) {
        return new ResponseEntity<>(DefaultRSMapper.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchPostException.class)
    protected ResponseEntity<?> handleNoSuchUserException(NoSuchPostException ex) {
        return new ResponseEntity<>(DefaultRSMapper.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
