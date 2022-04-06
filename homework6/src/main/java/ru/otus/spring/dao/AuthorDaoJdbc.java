package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.dao.exception.ObjectNotFoundException;
import ru.otus.spring.dao.jdbc.context.CommonContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final CommonContext context;

    @Override
    public long count(AuthorFilter filter) {
        return context.getAuthorsCount(filter);
    }

    @Override
    public Author get(AuthorId id) {
        AuthorFilter filter = AuthorFilter.builder()
                .authorIds(Set.of(id))
                .build();
        List<Author> authors = context.getAuthors(filter);
        if (authors.size() == 0) {
            throw new ObjectNotFoundException(String.format("Author with id %s is not found", id.getAuthorId()));
        }
        return authors.get(0);
    }

    @Override
    public List<Author> get(AuthorFilter filter) {
        return context.getAuthors(filter);
    }

    @Override
    public AuthorId insert(Author author) {
        return context.insertAuthor(author);
    }

    @Override
    public AuthorId update(Author author) {
        return context.updateAuthor(author);
    }

    @Override
    public AuthorId delete(AuthorId id) {
        return context.deleteAuthor(id);
    }
}
