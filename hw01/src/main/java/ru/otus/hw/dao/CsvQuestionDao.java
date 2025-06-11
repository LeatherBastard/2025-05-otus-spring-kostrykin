package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class CsvQuestionDao implements QuestionDao {
    private static final String FILE_NOT_FOUND_EXCEPTION_MESSAGE = "File not found";

    private final TestFileNameProvider fileNameProvider;

    private final InputStream inputStream;

    public CsvQuestionDao(TestFileNameProvider fileNameProvider) {
        this.fileNameProvider = fileNameProvider;
        inputStream = fileNameProvider.getClass()
                .getClassLoader()
                .getResourceAsStream(this.fileNameProvider
                        .getTestFileName());
        if (inputStream == null) {
            throw new QuestionReadException(FILE_NOT_FOUND_EXCEPTION_MESSAGE);
        }

    }

    @Override
    public List<Question> findAll() {
        return new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                .withSkipLines(1)
                .withType(QuestionDto.class)
                .withSeparator(';')
                .build().parse()
                .stream().map(QuestionDto::toDomainObject)
                .toList();
    }
}
