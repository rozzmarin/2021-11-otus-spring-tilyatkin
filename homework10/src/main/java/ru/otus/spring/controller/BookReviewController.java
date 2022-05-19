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
import ru.otus.spring.dto.BookReviewDto;
import ru.otus.spring.service.BookReviewService;
import ru.otus.spring.service.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class BookReviewController {
    private final BookService bookService;
    private final BookReviewService bookReviewService;

    @GetMapping("/books/{bookId}/reviews")
    public String readBookReviews(
            @PathVariable("bookId") BookId bookId,
            @ModelAttribute BookReviewFilter bookReviewFilter,
            Model model
    ) {
        Book book = bookService.find(bookId);
        bookReviewFilter.setBookIds(Set.of(book.getBookId()));
        List<BookReview> bookReviews = bookReviewService.find(bookReviewFilter);
        model.addAttribute("ratingLevels", RatingLevel.values());
        model.addAttribute("bookReviewFilter", bookReviewFilter);
        model.addAttribute("bookReviews", bookReviews);
        model.addAttribute("bookId", book.getBookId());
        model.addAttribute("bookTitle", BookDto.titleFromDomain(book));
        return "bookReviews/list";
    }

    @GetMapping("/books/{bookId}/reviews/add")
    public String addBookReview(
            @PathVariable("bookId") BookId bookId,
            Model model
    ) {
        Book book = bookService.find(bookId);
        BookReviewDto bookReview = new BookReviewDto();
        bookReview.setBookId(book.getBookId());
        bookReview.setBookTitle(BookDto.titleFromDomain(book));
        model.addAttribute("bookReview", bookReview);
        return "bookReviews/add";
    }

    @PostMapping("/books/{bookId}/reviews/add")
    public String addBookReview(
            @PathVariable("bookId") BookId bookId,
            @ModelAttribute("bookReview") @Valid BookReviewDto bookReview,
            BindingResult result,
            Model model
    ) {
        Book book = bookService.find(bookId);
        bookReview.setBookId(book.getBookId());
        bookReview.setBookTitle(BookDto.titleFromDomain(book));
        if (result.hasErrors()) {
            return "bookReviews/add";
        }
        bookReviewService.add(bookReview.toDomain());
        return "redirect:/books/{bookId}/reviews";
    }

    @GetMapping("/books/{bookId}/reviews/{reviewId}")
    public String editBookReview(
            @PathVariable("bookId") BookId bookId,
            @PathVariable("reviewId") BookReviewId reviewId,
            Model model
    ) {
        BookReviewDto bookReview = BookReviewDto.fromDomain(bookReviewService.find(reviewId));
        model.addAttribute("bookReview", bookReview);
        return "bookReviews/edit";
    }

    @PostMapping("/books/{bookId}/reviews/{reviewId}")
    public String editBookReview(
            @PathVariable("bookId") BookId bookId,
            @PathVariable("reviewId") BookReviewId reviewId,
            @ModelAttribute("bookReview") @Valid BookReviewDto bookReviewDto,
            BindingResult result,
            Model model
    ) {
        Book book = bookService.find(bookId);
        bookReviewDto.setBookReviewId(reviewId);
        bookReviewDto.setBookId(book.getBookId());
        if (result.hasErrors()) {
            return "bookReviews/edit";
        }
        bookReviewService.edit(bookReviewDto.toDomain());
        return "redirect:/books/{bookId}/reviews";
    }

    @PostMapping("/books/{bookId}/reviews/{reviewId}/remove")
    public String removeBook(
            @PathVariable("bookId") BookId bookId,
            @PathVariable("reviewId") BookReviewId reviewId
    ) {
        bookReviewService.remove(reviewId);
        return "redirect:/books/{bookId}/reviews";
    }
}