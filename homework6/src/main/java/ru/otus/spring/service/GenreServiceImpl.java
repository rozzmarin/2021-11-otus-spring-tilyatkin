package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao dao;

    @Override
    public Genre find(GenreId id) {
        return dao.get(id);
    }

    @Override
    public List<Genre> find(GenreFilter filter) {
        return dao.get(filter);
    }

    @Override
    public Genre add(Genre genre) {
        GenreId genreId = dao.insert(genre);
        if (genreId != null) {
            return dao.get(genreId);
        }
        return null;
    }

    @Override
    public Genre edit(Genre genre) {
        GenreId genreId = dao.update(genre);
        if (genreId != null) {
            return dao.get(genreId);
        }
        return null;
    }

    @Override
    public GenreId remove(GenreId id) {
        return dao.delete(id);
    }
}
