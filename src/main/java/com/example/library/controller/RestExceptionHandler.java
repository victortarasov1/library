package com.example.library.controller;

import com.example.library.dto.ApiError;
import com.example.library.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {
    /*
     * handle exceptions on Service - layer
     */
    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(RuntimeException ex, WebRequest request) {
        ApiError apiError = new ApiError("entity not found exception", List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthorContainsBookException.class, AuthorDoesntContainsBookException.class, AuthorNotUniqueException.class})
    protected ResponseEntity<Object> handleDataNotAcceptableEx(RuntimeException ex) {
        ApiError apiError = new ApiError("This data is not acceptable!", List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    /*
     * handle validation messages
     *
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).toList();
        ApiError apiError = new ApiError("validation error", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /*
     * handle ill-defined (bad JSON) data
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError("Malformed JSON Request", List.of(Objects.requireNonNull(ex.getMessage())));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /*
     * handle incorrect data - types
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var apiError = new ApiError("bad argument type", List.of(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName())));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /*
     * if no handler is found for this request.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiError apiError = new ApiError("No Handler Found", List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }


}
