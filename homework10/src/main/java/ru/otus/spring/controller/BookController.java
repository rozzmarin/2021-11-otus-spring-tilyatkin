package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.domain.*;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping(path = { "/books", "/" })
    public String readBooks(
            @ModelAttribute BookFilter bookFilter,
            Model model
    ) {
        List<Book> books = bookService.find(bookFilter);
        model.addAttribute("bookFilter", bookFilter);
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/books/add")
    public String addBook(
            Model model
    ) {
        List<Author> authors = authorService.find(AuthorFilter.builder().build());
        List<Genre> genres = genreService.find(GenreFilter.builder().build());
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "books/add";
    }

    @PostMapping("/books/add")
    public String addBook(
            @ModelAttribute("book") @Valid BookDto book,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            List<Author> authors = authorService.find(AuthorFilter.builder().build());
            List<Genre> genres = genreService.find(GenreFilter.builder().build());
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "books/add";
        }
        bookService.add(book.toDomain());
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String editBook(
            @PathVariable("id") BookId id,
            Model model
    ) {
        BookDto book = BookDto.fromDomain(bookService.find(id));
        List<Author> authors = authorService.find(AuthorFilter.builder().build());
        List<Genre> genres = genreService.find(GenreFilter.builder().build());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "books/edit";
    }

    @PostMapping("/books/{id}")
    public String editBook(
            @PathVariable("id") BookId id,
            @ModelAttribute("book") @Valid BookDto book,
            BindingResult result,
            Model model
    ) {
        book.setBookId(id);
        if (result.hasErrors()) {
            List<Author> authors = authorService.find(AuthorFilter.builder().build());
            List<Genre> genres = genreService.find(GenreFilter.builder().build());
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "books/edit";
        }
        bookService.edit(book.toDomain());
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/remove")
    public String removeBook(
            @PathVariable("id") BookId id
    ) {
        bookService.remove(id);
        return "redirect:/books";
    }
}