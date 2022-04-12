package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.*;
import ru.otus.spring.service.BookReviewService;
import ru.otus.spring.printer.Printer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("book-reviews")
@RequiredArgsConstructor
public class BookReviewCommands {
    private final BookReviewService bookReviewService;
    private final Printer<BookReview> bookReviewPrinter;

    @ShellMethod(value = "Get book reviews", key = {"book-review-get"})
    public String getBookReviews(
            @ShellOption(help = "Book's id(s)", defaultValue = "") Set<BookId> bookIds,
            @ShellOption(help = "Book reviewer's name", defaultValue = "") String reviewerName,
            @ShellOption(help = "Book review rating level", defaultValue = "") Set<RatingLevel> ratingLevel
    ) {
        List<BookReview> bookReviews = bookReviewService.find(BookReviewFilter.builder()
                .bookIds(bookIds)
                .reviewerName(reviewerName)
                .ratingLevel(ratingLevel)
                .build());
        return bookReviews
                .stream()
                .map(bookReviewPrinter::fullPrint)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Add book review", key = {"book-review-add"})
    public String addBookReview(
            @ShellOption(help = "Book's id") BookId bookId,
            @ShellOption(help = "Book reviewer's name") String reviewerName,
            @ShellOption(help = "Book review rating") Integer rating,
            @ShellOption(help = "Book review comment") String comment
    ) {
        BookReview newBookReview = bookReviewService.add(BookReview.builder()
                .book(new Book(bookId))
                .reviewerName(reviewerName)
                .rating(rating)
                .comment(comment)
                .build());
        return newBookReview != null ?
                bookReviewPrinter.fullPrint(newBookReview) :
                "Unable to add book review";
    }

    @ShellMethod(value = "Edit book review", key = {"book-review-edit"})
    public String editBookReview(
            @ShellOption(help = "Book review id") BookReviewId bookReviewId,
            @ShellOption(help = "Book reviewer's name", defaultValue = "") String reviewerName,
            @ShellOption(help = "Book review rating", defaultValue = "") Integer rating,
            @ShellOption(help = "Book review comment", defaultValue = "") String comment
    ) {
        BookReview bookReview = bookReviewService.find(bookReviewId);
        if (!reviewerName.equals("")) {
            bookReview.setReviewerName(reviewerName);
        }
        if (rating != null) {
            bookReview.setRating(rating);
        }
        if (!comment.equals("")) {
            bookReview.setComment(comment);
        }
        BookReview newBookReview = bookReviewService.edit(bookReview);
        return newBookReview != null ?
                bookReviewPrinter.fullPrint(newBookReview) :
                "Unable to edit book review";
    }

    @ShellMethod(value = "Remove book review", key = {"book-review-remove"})
    public String removeBookReview(
            @ShellOption(help = "Book review id") BookReviewId bookReviewId
    ) {
        BookReviewId oldBookReviewId = bookReviewService.remove(bookReviewId);
        return oldBookReviewId != null ?
                "Book review has been removed" :
                "Unable to remove book review";
    }
}