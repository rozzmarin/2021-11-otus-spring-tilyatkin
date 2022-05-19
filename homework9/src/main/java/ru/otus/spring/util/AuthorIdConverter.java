package ru.otus.spring.util;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorId;

@Component
public class AuthorIdConverter extends BaseGenericConverter<AuthorId>  {
    public AuthorIdConverter() {
        super(AuthorId.class);
    }

    @Override
    protected AuthorId fromString(String source) {
        Long authorId = LongUtils.parseOrNull(source);
        if (authorId == null)
            return null;
        return new AuthorId(authorId);
    }

    @Override
    protected String toString(AuthorId source) {
        if (source == null)
            return null;
        return Long.toString(source.getAuthorId());
    }
}