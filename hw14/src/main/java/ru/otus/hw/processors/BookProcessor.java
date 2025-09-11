package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.mongo.BookDocument;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, BookDocument> {
    private final BookConverter bookConverter;

    @Override
    public BookDocument process(Book book) throws Exception {
        return bookConverter.bookToBookDocument(book);
    }


}
