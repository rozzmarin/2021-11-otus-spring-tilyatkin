package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.*;
import ru.otus.spring.service.BookService;
import ru.otus.spring.printer.Printer;

import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("books")
@RequiredArgsConstructor
public class BookCommands {
    private final BookService bookService;
    private final Printer<Book> bookPrinter;

    @ShellMethod(value = "Get books", key = {"book-get"})
    public String getBooks(
            @ShellOption(help = "Book's id(s)", defaultValue = "") Set<BookId> bookIds,
            @ShellOption(help = "Book's title", defaultValue = "") String title,
            @ShellOption(help = "Author's id(s)", defaultValue = "") Set<AuthorId> authorIds,
            @ShellOption(help = "Author's name", defaultValue = "") String authorName,
            @ShellOption(help = "Genre's id(s)", defaultValue = "") Set<GenreId> genreIds,
            @ShellOption(help = "Genre's title", defaultValue = "") String genreTitle
    ) {
        List<Book> books = bookService.find(BookFilter.builder()
                .bookIds(bookIds)
                .title(title)
                .authorFilter(AuthorFilter.builder()
                        .authorIds(authorIds)
                        .name(authorName)
                        .build())
                .genreFilter(GenreFilter.builder()
                        .genreIds(genreIds)
                        .title(genreTitle)
                        .build())
                .build());
        return books
                .stream()
                .map(bookPrinter::fullPrint)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Add book", key = {"book-add"})
    public String addBook(
            @ShellOption(help = "Book's title") String title,
            @ShellOption(help = "Book's author(s)") Set<AuthorId> authorIds,
            @ShellOption(help = "Book's genre(s)") Set<GenreId> genreIds
    ) {
        Set<Author> authors = authorIds.stream()
                .map(Author::new)
                .collect(Collectors.toSet());
        Set<Genre> genres = genreIds.stream()
                .map(Genre::new)
                .collect(Collectors.toSet());
        Book newBook = bookService.add(Book.builder()
                .title(title)
                .authors(authors)
                .genres(genres)
                .build());
        return newBook != null ?
                bookPrinter.fullPrint(newBook) :
                "Unable to add book";
    }

    @ShellMethod(value = "Edit book", key = {"book-edit"})
    public String editBook(
            @ShellOption(help = "Book's id") BookId bookId,
            @ShellOption(help = "Book's title", defaultValue = "") String title,
            @ShellOption(help = "Book's author(s)", defaultValue = "") Set<AuthorId> authorIds,
            @ShellOption(help = "Book's genre(s)", defaultValue = "") Set<GenreId> genreIds
    ) {
        Book book = bookService.find(bookId);
        if (book == null) {
            return "Unable to eidt book";
        }
        if (!title.equals("")) {
            book.setTitle(title);
        }
        if (authorIds != null) {
            book.setAuthors(authorIds.stream()
                    .map(Author::new)
                    .collect(Collectors.toSet()));
        }
        if (genreIds != null) {
            book.setGenres(genreIds.stream()
                    .map(Genre::new)
                    .collect(Collectors.toSet()));
        }
        Book newBook = bookService.edit(book);
        return newBook != null ?
                bookPrinter.fullPrint(newBook) :
                "Unable to edit book";
    }

    @ShellMethod(value = "Remove book", key = {"book-remove"})
    public String removeGenre(
            @ShellOption(help = "Book's id") BookId bookId
    ) {
        BookId oldBookId = bookService.remove(bookId);
        return oldBookId != null ?
                "Book has been removed" :
                "Unable to remove book";
    }
}
