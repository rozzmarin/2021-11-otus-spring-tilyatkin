package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public List<Book> readBooks(BookFilter bookFilter) {
        return bookService.find(bookFilter);
    }

    @GetMapping("/api/books/{id}")
    public BookDto readBook(@PathVariable("id") BookId id) {
        return BookDto.fromDomain(bookService.find(id));
    }

    @PostMapping("/api/books")
    public BookDto addBook(@RequestBody @Valid BookDto book) {
        return BookDto.fromDomain(bookService.add(book.toDomain()));
    }

    @PutMapping("/api/books/{id}")
    public BookDto editBook(@PathVariable("id") BookId id, @RequestBody @Valid BookDto book) {
        book.setBookId(id);
        return BookDto.fromDomain(bookService.edit(book.toDomain()));
    }

    @DeleteMapping("/api/books/{id}")
    public BookId removeBook(@PathVariable("id") BookId id) {
        return bookService.remove(id);
    }
}