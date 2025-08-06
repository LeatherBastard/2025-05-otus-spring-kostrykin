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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CreateBookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookDto;
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
            List<AuthorDto> authors = authorService.findAll();
            List<GenreDto> genres = genreService.findAll();
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            model.addAttribute("bookDto", bookDto);
            return "addbook";
        }
        bookService.insert(bookDto.title(), bookDto.authorId(), bookDto.genreIds());
        return "redirect:/";
    }

    @GetMapping("/book")
    public String addBookPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        CreateBookDto bookDto = new CreateBookDto("", 0, Set.of());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("bookDto", bookDto);
        return "addbook";
    }

    @GetMapping("/books/")
    public String bookByIdPage(@RequestParam long bookId, Model model) {
        BookDto book = bookService.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book with id %d was not found", bookId)));
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }

    @GetMapping("/books/{bookId}/edit")
    public String editBookPage(@PathVariable long bookId, Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        BookDto book = bookService.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book with id %d was not found", bookId)));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("comments", comments);
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
    public String updateBook(Model model, @Valid @ModelAttribute("bookDto") UpdateBookDto bookDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<AuthorDto> authors = authorService.findAll();
            List<GenreDto> genres = genreService.findAll();
            List<CommentDto> comments = commentService.findAllByBookId(bookDto.id());
            BookDto book = bookService.findById(bookDto.id()).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Book with id %d was not found", bookDto.id())));
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            model.addAttribute("comments", comments);
            model.addAttribute("book", book);
            return "editbook";
        }

        bookService.update(bookDto.id(), bookDto.title(), bookDto.authorId(), bookDto.genreIds());
        return "redirect:/";
    }

    @DeleteMapping("/books/{bookId}")
    public String deleteBook(@PathVariable long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }
}
