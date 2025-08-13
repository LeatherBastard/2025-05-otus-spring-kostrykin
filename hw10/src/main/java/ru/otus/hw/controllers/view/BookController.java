package ru.otus.hw.controllers.view;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping("/books")
    public String allBooksPage() {
        return "books";
    }


    @GetMapping("/book")
    public String addBookPage() {
        return "addbook";
    }

    @GetMapping("/books/")
    public String bookByIdPage(@RequestParam long bookId) throws Exception {
        bookService.findById(bookId);
        return "book";
    }


}
