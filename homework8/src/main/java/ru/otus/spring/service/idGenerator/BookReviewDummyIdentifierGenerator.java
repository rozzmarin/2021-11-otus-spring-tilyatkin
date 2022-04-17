package ru.otus.spring.service.idGenerator;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookReviewId;

@Component
public class BookReviewDummyIdentifierGenerator implements IdentifierGenerator<BookReviewId> {
    public BookReviewId generate() {
        return new BookReviewId(DummyIdentifierGenerator.generateTemplateString("BR", 6));
    }
}
