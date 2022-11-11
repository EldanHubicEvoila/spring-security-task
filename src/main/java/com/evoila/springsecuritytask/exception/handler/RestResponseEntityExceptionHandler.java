package com.evoila.springsecuritytask.exception.handler;


import com.evoila.springsecuritytask.exception.ResourceNotFoundException;
import com.evoila.springsecuritytask.exception.response.ErrorResponse;

import io.jsonwebtoken.MalformedJwtException;
import lombok.NonNull;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                        .builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timeStamp(new Date())
                        .message(ex.getMessage())
                        .description(request.getDescription(false))
                        .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timeStamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                           @NonNull HttpHeaders headers,
                                                                           @NonNull HttpStatus status,
                                                                           WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .validationErrors(errorList)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
