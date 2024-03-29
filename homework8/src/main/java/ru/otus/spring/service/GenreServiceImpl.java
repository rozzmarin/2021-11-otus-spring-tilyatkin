package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.*;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BaseSpecification;
import ru.otus.spring.repository.specification.GenreSpecification;
import ru.otus.spring.service.idGenerator.IdentifierGenerator;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;
    private final GenreSpecification specification;
    private final BookRepository bookRepository;
    private final BaseSpecification<BookFilter> bookSpecification;
    private final IdentifierGenerator<GenreId> genreIdGenerator;

    @Override
    @Transactional(readOnly = true)
    public Genre find(GenreId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Genre with id %s is not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> find(GenreFilter filter) {
        return repository.findAll(specification.toPredicate(filter));
    }

    @Override
    public Genre add(Genre genre) {
        genre.setGenreId(genreIdGenerator.generate());
        return repository.save(genre);
    }

    @Override
    public Genre edit(Genre genre) {
        return repository.save(genre);
    }

    @Override
    public GenreId remove(GenreId id) {
        BookFilter booksByGenreIdFilter = BookFilter.builder()
                .genreFilter(GenreFilter.builder()
                        .genreIds(Set.of(id))
                        .build())
                .build();
        long booksWithGenreIdCount = bookRepository.count(bookSpecification.toPredicate(booksByGenreIdFilter));
        if (booksWithGenreIdCount == 0) {
            repository.deleteById(id);
            return id;
        }
        return null;
    }
}
