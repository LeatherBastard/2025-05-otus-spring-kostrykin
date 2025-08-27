package ru.otus.hw.exceptions;

public class EnteredPasswordsNotMatchedException extends RuntimeException {
    public EnteredPasswordsNotMatchedException(String message) {
        super(message);
    }
}
