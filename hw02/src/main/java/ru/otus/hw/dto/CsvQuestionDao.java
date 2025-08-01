package ru.otus.hw.dto;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.dto.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final String FILE_NOT_FOUND_MESSAGE = "File not found!";

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream inputStream = fileNameProvider.getClass()
                .getClassLoader()
                .getResourceAsStream(this.fileNameProvider
                        .getTestFileName())) {
            if (inputStream == null) {
                throw new QuestionReadException(FILE_NOT_FOUND_MESSAGE);
            }
            return new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                    .withSkipLines(1)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .build().parse()
                    .stream().map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }
}
