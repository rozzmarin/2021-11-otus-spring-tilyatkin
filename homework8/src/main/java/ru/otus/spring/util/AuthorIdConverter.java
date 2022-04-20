package ru.otus.spring.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorId;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorIdConverter {
    @Component
    static class StringToAuthorIdConverter implements Converter<String, AuthorId> {
        @Override
        public AuthorId convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return new AuthorId(source);
        }
    }

    @Component
    static class StringToAuthorIdSetConverter implements Converter<String, Set<AuthorId>> {
        @Override
        public Set<AuthorId> convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return Arrays.stream(source.split(","))
                    .filter(s -> !s.isEmpty())
                    .map(AuthorId::new)
                    .collect(Collectors.toSet());
        }
    }
}