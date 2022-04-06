package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookPrinter implements Printer<Book> {
    private final Printer<Author> authorPrinter;
    private final Printer<Genre> genrePrinter;

    @Override
    public String print(Book object) {
        return String.format("%s", object.getTitle());
    }

    @Override
    public String printWithId(Book object) {
        String bookLabel = String.format("%d. %s", object.getBookId().getBookId(), object.getTitle());
        String genreLabel = object.getGenres()
                .stream()
                .map(genrePrinter::print)
                .collect(Collectors.joining(";"));
        String authorLabel = object.getAuthors()
                .stream()
                .map(a -> String.format("\t- %s", authorPrinter.print(a)))
                .collect(Collectors.joining("\n"));

        return String.format("%s (%s)\n%s", bookLabel, genreLabel, authorLabel);
    }
}
