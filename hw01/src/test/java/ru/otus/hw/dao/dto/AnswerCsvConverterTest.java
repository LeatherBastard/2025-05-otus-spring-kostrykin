package ru.otus.hw.dao.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnswerCsvConverterTest {

    private AnswerCsvConverter converter;

    @BeforeEach
    void initialize() {
        converter = new AnswerCsvConverter();
    }

    @Test
    void whenConvertToRead_thenReturnAnswer() {
        String actualValue = "Yes%false";
        Answer expectedAnswer = new Answer("Yes", false);
        assertEquals(expectedAnswer, converter.convertToRead(actualValue));
    }

    @Test
    void whenWrongConvertToRead_thenReturnException() {
        String actualValue = "YesDasda";
        assertThrows(IndexOutOfBoundsException.class, () -> converter.convertToRead(actualValue));
    }
}
