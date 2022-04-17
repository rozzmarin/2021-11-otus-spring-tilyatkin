package ru.otus.spring.printer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookReview;

@Service
@RequiredArgsConstructor
public class BookReviewPrinter implements Printer<BookReview> {
    private final Printer<Book> bookPrinter;

    @Override
    public String shortPrint(BookReview object) {
        return String.format("Автор рецензии: %s, оценка: %d", object.getReviewerName(), object.getRating());
    }

    @Override
    public String fullPrint(BookReview object) {
        Book book = object.getBook();

        return String.format("%S. %s\nАвтор рецензии: %s\nОценка: %d\n\t%s",
                object.getBookReviewId(),
                bookPrinter.shortPrint(book),
                object.getReviewerName(),
                object.getRating(),
                object.getComment());
    }
}
