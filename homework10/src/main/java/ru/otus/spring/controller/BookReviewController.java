package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.*;
import ru.otus.spring.dto.BookReviewDto;
import ru.otus.spring.dto.RatingLevelDto;
import ru.otus.spring.service.BookReviewService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookReviewController {
    private final BookReviewService bookReviewService;

    @GetMapping("/api/books/{bookId}/reviews")
    public List<BookReviewDto> readBookReviews(@PathVariable("bookId") BookId bookId, BookReviewFilter bookReviewFilter) {
        bookReviewFilter.setBookIds(Set.of(bookId));
        return bookReviewService.find(bookReviewFilter).stream()
                .map(BookReviewDto::fromDomainForList)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books/{bookId}/reviews/{reviewId}")
    public BookReviewDto readBookReview(@PathVariable("bookId") BookId bookId, @PathVariable("reviewId") BookReviewId reviewId) {
        return BookReviewDto.fromDomain(bookReviewService.find(reviewId));
    }

    @PostMapping("/api/books/{bookId}/reviews")
    public BookReviewDto addBookReview(@PathVariable("bookId") BookId bookId, @RequestBody @Valid BookReviewDto bookReview) {
        bookReview.setBookId(bookId);
        return BookReviewDto.fromDomain(bookReviewService.add(bookReview.toDomain()));
    }

    @PutMapping("/api/books/{bookId}/reviews/{reviewId}")
    public BookReviewDto editBookReview(
            @PathVariable("bookId") BookId bookId,
            @PathVariable("reviewId") BookReviewId reviewId,
            @RequestBody @Valid BookReviewDto bookReviewDto
    ) {
        bookReviewDto.setBookReviewId(reviewId);
        bookReviewDto.setBookId(bookId);
        return BookReviewDto.fromDomain(bookReviewService.edit(bookReviewDto.toDomain()));
    }

    @DeleteMapping("/api/books/{bookId}/reviews/{reviewId}")
    public BookReviewId removeBook(@PathVariable("bookId") BookId bookId, @PathVariable("reviewId") BookReviewId reviewId) {
        return bookReviewService.remove(reviewId);
    }

    @GetMapping("/api/rating/levels")
    public List<RatingLevelDto> readRatingLevels() {
        return Arrays.stream(RatingLevel.values())
                .map(RatingLevelDto::fromDomain)
                .collect(Collectors.toList());
    }
}