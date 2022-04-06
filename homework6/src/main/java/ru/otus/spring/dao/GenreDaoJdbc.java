package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.dao.jdbc.context.CommonContext;
import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final CommonContext context;

    @Override
    public long count(GenreFilter filter) {
        return context.getGenresCount(filter);
    }

    @Override
    public Genre get(GenreId id) {
        GenreFilter filter = GenreFilter.builder()
                .genreIds(Set.of(id))
                .build();
        List<Genre> genres = context.getGenres(filter);
        if (genres.size() == 0) {
            throw new ObjectNotFoundException(String.format("Genre with id %s is not found", id.getGenreId()));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> get(GenreFilter filter) {
        return context.getGenres(filter);
    }

    @Override
    public GenreId insert(Genre genre) {
        return context.insertGenre(genre);
    }

    @Override
    public GenreId update(Genre genre) {
        return context.updateGenre(genre);
    }

    @Override
    public GenreId delete(GenreId id) {
        return context.deleteGenre(id);
    }
}
