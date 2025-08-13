package ru.otus.hw.controllers.view;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFoundException(EntityNotFoundException ex) {
        return new ModelAndView("customError", Map.of("errorText", ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ModelAndView("customError", Map.of("errorText", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return new ModelAndView("customError", Map.of("errorText", ex.getMessage() + " " +
                Arrays.toString(ex.getStackTrace())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
