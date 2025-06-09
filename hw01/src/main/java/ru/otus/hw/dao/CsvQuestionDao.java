package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

        FileReader fileReader;
        try {
            fileReader = new FileReader(fileNameProvider.getTestFileName());
        } catch (FileNotFoundException e) {
            throw new QuestionReadException(e.getMessage(), e);
        }

        List<Question> questions = new CsvToBeanBuilder<QuestionDto>(fileReader)
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build().parse()
                .stream().map(QuestionDto::toDomainObject)
                .toList();
        return questions;
    }
}
