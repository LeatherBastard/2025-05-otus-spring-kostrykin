package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.otus.hw.dto.error.ApiError;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
@Log4j2
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


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex) {
        log.error(Arrays.stream(ex.getStackTrace())
                .map(element -> element.toString()).toList().toString());
        return new ApiError(INTERNAL_SERVER_ERROR_STATUS, INTERNAL_SERVER_ERROR_REASON,
                ex.getMessage(), LocalDateTime.now());
    }
}
