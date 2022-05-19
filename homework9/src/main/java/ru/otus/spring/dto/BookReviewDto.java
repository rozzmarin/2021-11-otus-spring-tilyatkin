package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.BookId;
import ru.otus.spring.domain.BookReview;
import ru.otus.spring.domain.BookReviewId;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookReviewDto {
    private BookReviewId bookReviewId;

    @NotNull(message = "Необходимо указать книгу")
    private BookId bookId;

    private String bookTitle;

    @NotNull(message = "Имя автора отзыва не может быть пустым")
    @Size(min = 1, max = 255, message = "Имя автора отзыва должно быть длиной от 1 до 255 символов")
    private String reviewerName;

    @Min(value = 1, message = "Оценка должна быть от 1 до 10")
    @Max(value = 10, message = "Оценка должна быть от 1 до 10")
    private int rating;

    @NotNull(message = "Комментарий не может быть пустым")
    @Size(min = 1, max = 4000, message = "Комментарий должен быть длиной от 1 до 4000 символов")
    private String comment;

    public BookReview toDomain() {
        return BookReview.builder()
                .bookReviewId(bookReviewId)
                .book(new Book(bookId))
                .reviewerName(reviewerName)
                .rating(rating)
                .comment(comment)
                .build();
    }

    public static BookReviewDto fromDomain(BookReview bookReview) {
        return new BookReviewDto(
                bookReview.getBookReviewId(),
                bookReview.getBook().getBookId(),
                BookDto.titleFromDomain(bookReview.getBook()),
                bookReview.getReviewerName(),
                bookReview.getRating(),
                bookReview.getComment());
    }
}
