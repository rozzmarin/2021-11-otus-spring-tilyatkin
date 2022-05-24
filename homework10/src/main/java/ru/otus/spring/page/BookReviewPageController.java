package ru.otus.spring.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.domain.BookReviewId;

@Controller
@RequiredArgsConstructor
public class BookReviewPageController {
    @GetMapping("/books/{bookId}/reviews")
    public String readBookReviews(@PathVariable("bookId") BookId bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "bookReviews/list";
    }

    @GetMapping("/books/{bookId}/reviews/add")
    public String addBookReview(@PathVariable("bookId") BookId bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "bookReviews/edit";
    }

    @GetMapping("/books/{bookId}/reviews/{reviewId}")
    public String editBookReview(@PathVariable("bookId") BookId bookId, @PathVariable("reviewId") BookReviewId reviewId, Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookReviewId", reviewId);
        return "bookReviews/edit";
    }
}