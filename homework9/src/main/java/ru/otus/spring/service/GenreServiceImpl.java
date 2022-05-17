package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.exception.InvalidOperationException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.domain.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.GenreSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Genre find(GenreId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Женр с кодом %s не найден", id.getGenreId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> find(GenreFilter filter) {
        return repository.findAll(GenreSpecification.of(filter));
    }

    @Override
    @Transactional
    public Genre add(Genre genre) {
        if (genre.getGenreId() != null) {
            throw new InvalidOperationException("Недопустимый идентификатор");
        }
        return repository.save(genre);
    }

    @Override
    @Transactional
    public Genre edit(Genre genre) {
        if (genre.getGenreId() == null) {
            throw new InvalidOperationException("Не задан идентификатор");
        }
        if (repository.findById(genre.getGenreId()).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Женр с кодом %s не найден", genre.getGenreId().getGenreId()));
        }
        return repository.save(genre);
    }

    @Override
    @Transactional
    public GenreId remove(GenreId id) {
        if (repository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Женр с кодом %s не найден", id.getGenreId()));
        }
        if (bookRepository.existsByGenresGenreId(id)) {
            throw new InvalidOperationException("Нельзя удалить жанр, у которого имеются книги");
        }
        repository.deleteById(id);
        return id;
    }
}
