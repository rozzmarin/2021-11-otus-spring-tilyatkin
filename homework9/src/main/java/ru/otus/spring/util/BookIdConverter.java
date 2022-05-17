package ru.otus.spring.util;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookId;

@Component
public class BookIdConverter extends BaseGenericConverter<BookId>  {
    public BookIdConverter() {
        super(BookId.class);
    }

    @Override
    protected BookId fromString(String source) {
        Long bookId = LongUtils.parseOrNull(source);
        if (bookId == null)
            return null;
        return new BookId(bookId);
    }

    @Override
    protected String toString(BookId source) {
        if (source == null)
            return null;
        return Long.toString(source.getBookId());
    }
}