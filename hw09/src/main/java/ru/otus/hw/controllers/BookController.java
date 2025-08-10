package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.CreateBookDto;
import ru.otus.hw.dto.book.UpdateBookDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/books")
    public String allBooksPage(Model model) {
        List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "books";
    }

    @PostMapping("/book")
    public String addBook(Model model, @Valid @ModelAttribute("bookDto") CreateBookDto bookDto,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("bookDto", bookDto);
            return "addbook";
        }
        bookService.insert(bookDto);
        return "redirect:/";
    }

    @GetMapping("/book")
    public String addBookPage(Model model) {
        CreateBookDto bookDto = new CreateBookDto("", 0, Set.of());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("bookDto", bookDto);
        return "addbook";
    }

    @GetMapping("/books/")
    public String bookByIdPage(@RequestParam long bookId, Model model) throws Exception {
        BookDto book = bookService.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book with id %d was not found", bookId)));
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";

    }

    @GetMapping("/books/{bookId}/edit")
    public String editBookPage(@PathVariable long bookId, Model model) {
        BookDto book = bookService.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book with id %d was not found", bookId)));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("comments", commentService.findAllByBookId(bookId));
        model.addAttribute("book", book);
        model.addAttribute("bookDto",
                new UpdateBookDto(book.id(),
                        book.title(),
                        book.author().id(),
                        book.genres().stream().map(genre -> genre.id()).collect(Collectors.toSet()
                        )));
        return "editbook";
    }

    @PutMapping("/books/{bookId}/edit")
    public String updateBook(Model model, @Valid @ModelAttribute("bookDto") UpdateBookDto bookDto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            BookDto book = bookService.findById(bookDto.id()).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Book with id %d was not found", bookDto.id())));
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("comments", commentService.findAllByBookId(bookDto.id()));
            model.addAttribute("book", book);
            return "editbook";
        }

        bookService.update(bookDto);
        return "redirect:/";
    }

    @DeleteMapping("/books/{bookId}")
    public String deleteBook(@PathVariable long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }
}
