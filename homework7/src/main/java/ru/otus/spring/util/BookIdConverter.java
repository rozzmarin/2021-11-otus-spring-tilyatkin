package ru.otus.spring.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.domain.BookId;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BookIdConverter {
    @Component
    static class BookIdToStringConverter implements Converter<String, BookId> {
        @Override
        public BookId convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return new BookId(Long.parseLong(source));
        }
    }

    @Component
    static class StringToBookIdSetConverter implements Converter<String, Set<BookId>> {
        @Override
        public Set<BookId> convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return Arrays.stream(source.split(","))
                    .filter(s -> !s.isEmpty())
                    .map(s -> new BookId(Long.parseLong(s)))
                    .collect(Collectors.toSet());
        }
    }
}