package ru.otus.spring.service.idGenerator;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookId;

@Component
public class BookDummyIdentifierGenerator implements IdentifierGenerator<BookId> {
    public BookId generate() {
        return new BookId(DummyIdentifierGenerator.generateTemplateString("B", 4));
    }
}
