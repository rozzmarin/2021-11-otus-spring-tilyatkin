package ru.otus.spring.util;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.domain.GenreId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreIdConverter extends BaseGenericConverter<GenreId>  {
    public GenreIdConverter() {
        super(GenreId.class);
    }

    @Override
    protected GenreId fromString(String source) {
        Long genreId = LongUtils.parseOrNull(source);
        if (genreId == null)
            return null;
        return new GenreId(genreId);
    }

    @Override
    protected String toString(GenreId source) {
        if (source == null)
            return null;
        return Long.toString(source.getGenreId());
    }

    /*
    @Component
    static class StringToGenreIdConverter implements Converter<String, GenreId> {
        @Override
        public GenreId convert(String source) {
            Long genreId = LongUtils.parseOrNull(source);
            if (genreId == null)
                return null;
            return new GenreId(genreId);
        }
    }

    @Component
    static class StringToGenreIdSetConverter implements Converter<String, Set<GenreId>> {
        @Override
        public Set<GenreId> convert(String source) {
            Set<GenreId> genreIds = new HashSet<>();
            for (String part : source.split(",")) {
                Long genreId = LongUtils.parseOrNull(part);
                if (genreId == null)
                    return null;
                genreIds.add(new GenreId(genreId));
            }
            return genreIds;
        }
    }

    @Component
    static class GenreIdSetToStringConverter implements Converter<Set<GenreId>, String>, ConditionalConverter {
        @Override
        public String convert(Set<GenreId> source) {
            if (source == null)
                return null;
            return source.stream()
                    .map(genreId -> Long.toString(genreId.getGenreId()))
                    .collect(Collectors.joining(","));
        }

        @Override
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            return sourceType.getType() == Set.class
                    && sourceType.getElementTypeDescriptor() != null && sourceType.getElementTypeDescriptor().getType() == GenreId.class
                    && targetType.getType() == String.class;

        }
    }

     */
}