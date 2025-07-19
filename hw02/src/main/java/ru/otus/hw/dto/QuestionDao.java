package ru.otus.hw.dto;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
