package com.bci.api.controller;

import com.bci.api.dto.errors.ErrorResponseDto;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.Objects;

import static com.bci.api.dto.errors.ErrorResponseDto.createError;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class AuthApiExceptionHandler {

    private static final String INVALID_TOKEN = "Invalid Token";
    private static final String ENTITY_NOT_FOUND = "Entity not found";
    private static final String ENTITY_ALREADY_EXISTS = "Entity already exists";
    private static final String INVALID_JSON = "Invalid JSON";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handle(MethodArgumentNotValidException ex) {
        final FieldError fieldError = ex.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return returnError(BAD_REQUEST,
                createError(4001, Objects.isNull(defaultMessage) ? ex.getMessage() : defaultMessage));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDto> handle(HttpMessageNotReadableException ex) {
        return returnError(BAD_REQUEST,
                createError(4000, INVALID_JSON));
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorResponseDto> handle(EntityExistsException ex) {
        return returnError(CONFLICT,
                createError(4090, ENTITY_ALREADY_EXISTS));
    }

    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<ErrorResponseDto> handle(SignatureException ex) {
        return returnError(UNAUTHORIZED,
                createError(4010, INVALID_TOKEN));
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<ErrorResponseDto> handle(MissingRequestHeaderException ex) {
        return returnError(UNAUTHORIZED,
                createError(4011, INVALID_TOKEN));
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<ErrorResponseDto> handle(InsufficientAuthenticationException ex) {
        return returnError(UNAUTHORIZED,
                createError(4011, INVALID_TOKEN));
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ErrorResponseDto> handle(MalformedJwtException ex) {
        return returnError(UNAUTHORIZED,
                createError(4012, INVALID_TOKEN));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handle(EntityNotFoundException ex) {
        return returnError(NOT_FOUND,
                createError(4040, ENTITY_NOT_FOUND));
    }

    private ResponseEntity<ErrorResponseDto> returnError(HttpStatus statusCode, ErrorResponseDto errorToReturn) {
        return ResponseEntity.status(statusCode).body(errorToReturn);
    }

}
