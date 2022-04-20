package ru.otus.spring.service.idGenerator;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.GenreId;

@Component
public class GenreDummyIdentifierGenerator implements IdentifierGenerator<GenreId> {
    public GenreId generate() {
        return new GenreId(DummyIdentifierGenerator.generateTemplateString("G", 4));
    }
}
