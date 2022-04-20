package ru.otus.spring.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.GenreId;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreIdConverter {
    @Component
    static class StringToGenreIdConverter implements Converter<String, GenreId> {
        @Override
        public GenreId convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return new GenreId(source);
        }
    }

    @Component
    static class StringToGenreIdSetConverter implements Converter<String, Set<GenreId>> {
        @Override
        public Set<GenreId> convert(String source) {
            if (StringUtils.isNullOrEmpty(source))
                return null;
            return Arrays.stream(source.split(","))
                    .filter(s -> !s.isEmpty())
                    .map(GenreId::new)
                    .collect(Collectors.toSet());
        }
    }
}