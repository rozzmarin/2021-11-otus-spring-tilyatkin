package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.GenreSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Genre find(GenreId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Genre with id %s is not found", id.getGenreId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> find(GenreFilter filter) {
        return repository.findAll(GenreSpecification.of(filter));
    }

    @Override
    public Genre add(Genre genre) {
        return repository.save(genre);
    }

    @Override
    public Genre edit(Genre genre) {
        return repository.save(genre);
    }

    @Override
    public GenreId remove(GenreId id) {
        repository.deleteById(id);
        return id;
    }
}
