package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final LocalizedMessagesService localizedMessagesService;

    public CsvQuestionDao(
            TestFileNameProvider fileNameProvider,
            @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService localizedMessagesService) {
        this.fileNameProvider = fileNameProvider;
        this.localizedMessagesService = localizedMessagesService;
    }

    @Override
    public List<Question> findAll() {
        try (InputStream inputStream = fileNameProvider.getClass()
                .getClassLoader()
                .getResourceAsStream(this.fileNameProvider
                        .getTestFileName())) {
            if (inputStream == null) {
                throw new QuestionReadException(localizedMessagesService.getMessage("QuestionDao.file.not.found"));
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
