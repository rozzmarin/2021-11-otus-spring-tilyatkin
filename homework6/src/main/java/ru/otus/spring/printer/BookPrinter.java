package ru.otus.spring.printer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookPrinter implements Printer<Book> {
    private final Printer<Author> authorPrinter;
    private final Printer<Genre> genrePrinter;

    @Override
    public String shortPrint(Book object) {
        String authorLabel = object.getAuthors()
                .stream()
                .map(authorPrinter::shortPrint)
                .collect(Collectors.joining(", "));
        return String.format("%s. %s", authorLabel, object.getTitle());
    }

    @Override
    public String fullPrint(Book object) {
        String authorLabel = object.getAuthors()
                .stream()
                .map(authorPrinter::shortPrint)
                .collect(Collectors.joining(", "));
        String genreLabel = object.getGenres()
                .stream()
                .map(genrePrinter::shortPrint)
                .collect(Collectors.joining(","));
        return String.format("%d. %s. %s (%s), %d рецензий",
                object.getBookId().getBookId(),
                authorLabel,
                object.getTitle(),
                genreLabel,
                object.getReviewsCount());
    }
}