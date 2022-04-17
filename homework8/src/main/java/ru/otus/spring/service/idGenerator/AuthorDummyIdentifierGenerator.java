package ru.otus.spring.service.idGenerator;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorId;

@Component
public class AuthorDummyIdentifierGenerator implements IdentifierGenerator<AuthorId> {
    public AuthorId generate() {
        return new AuthorId(DummyIdentifierGenerator.generateTemplateString("A", 4));
    }
}
