package com.celonis.challenge.controllers;

import com.celonis.challenge.exceptions.BadRequestError;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotAuthorizedException;
import com.celonis.challenge.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFound() {
        logger.warn("Entity not found");
        return new ResponseEntity(new NotFoundException(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity handleNotAuthorized() {
        logger.warn("Not authorized");
        return new ResponseEntity(new NotAuthorizedException(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleInternalError(Exception e) {
        logger.error("Unhandled Exception in Controller", e);
        return new ResponseEntity(new InternalException(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleClientError(Exception e) {
        logger.error("Bad Request in Controller", e);
        return new ResponseEntity(new BadRequestError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
