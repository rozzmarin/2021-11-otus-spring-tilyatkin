package ru.otus.spring.util;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookReviewId;

@Component
public class BookReviewIdConverter extends BaseGenericConverter<BookReviewId>  {
    public BookReviewIdConverter() {
        super(BookReviewId.class);
    }

    @Override
    protected BookReviewId fromString(String source) {
        Long bookReviewIdId = LongUtils.parseOrNull(source);
        if (bookReviewIdId == null)
            return null;
        return new BookReviewId(bookReviewIdId);
    }

    @Override
    protected String toString(BookReviewId source) {
        if (source == null)
            return null;
        return Long.toString(source.getBookReviewId());
    }
}