package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.BookId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookReviewCount {
    private BookId bookId;
    private long reviewsCount;
}
