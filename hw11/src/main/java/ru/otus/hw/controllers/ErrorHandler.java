package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MissingRequestValueException;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandler {

    private static final String BAD_REQUEST_STATUS = "BAD_REQUEST";

    private static final String BAD_REQUEST_REASON = "Incorrectly made request.";

    private static final String NOT_FOUND_STATUS = "NOT_FOUND";

    private static final String NOT_FOUND_REASON = "The required object was not found.";

    private static final String INTERNAL_SERVER_ERROR_STATUS = "INTERNAL_SERVER_ERROR";

    private static final String INTERNAL_SERVER_ERROR_REASON = "Internal server error.";

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(EntityNotFoundException ex) {
        return new ApiError(NOT_FOUND_STATUS, NOT_FOUND_REASON, ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleWebExchangeBindException(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingRequestValueException(MissingRequestValueException ex) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, ex.getMessage(), LocalDateTime.now());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Throwable ex) {
        return new ApiError(INTERNAL_SERVER_ERROR_STATUS, INTERNAL_SERVER_ERROR_REASON,
                ex.getMessage() + Arrays.stream(ex.getStackTrace())
                        .map(element -> element.toString()).toList().toString(), LocalDateTime.now());
    }
}
