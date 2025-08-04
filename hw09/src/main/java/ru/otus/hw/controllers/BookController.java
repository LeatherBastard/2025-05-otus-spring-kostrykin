package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/books")
    public String allBooksPage(Model model) {
        List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "books";
    }

    @GetMapping("/book")
    public String bookByIdPage(@RequestParam long id, Model model) {
        BookDto book = bookService.findById(id).orElseThrow(EntityNotFoundException::new);
        List<CommentDto> comments = commentService.findAllByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }
}
