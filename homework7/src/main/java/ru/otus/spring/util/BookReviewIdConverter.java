package ru.otus.spring.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.BookReviewId;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BookReviewIdConverter {
    @Component
    static class BookReviewIdToStringConverter implements Converter<String, BookReviewId> {
        @Override
        public BookReviewId convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return new BookReviewId(Long.parseLong(source));
        }
    }

    @Component
    static class StringToBookReviewIdSetConverter implements Converter<String, Set<BookReviewId>> {
        @Override
        public Set<BookReviewId> convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return Set.of();
            return Arrays.stream(source.split(","))
                    .filter(s -> !s.isEmpty())
                    .map(s -> new BookReviewId(Long.parseLong(s)))
                    .collect(Collectors.toSet());
        }
    }
}