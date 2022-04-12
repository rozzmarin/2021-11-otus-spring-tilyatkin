package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Genre find(GenreId id) {
        return repository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> find(GenreFilter filter) {
        return repository.get(filter);
    }

    @Override
    public Genre add(Genre genre) {
        GenreId genreId = repository.insert(genre);
        if (genreId != null) {
            return repository.get(genreId);
        }
        return null;
    }

    @Override
    public Genre edit(Genre genre) {
        GenreId genreId = repository.update(genre);
        if (genreId != null) {
            return repository.get(genreId);
        }
        return null;
    }

    @Override
    public GenreId remove(GenreId id) {
        return repository.delete(id);
    }
}
