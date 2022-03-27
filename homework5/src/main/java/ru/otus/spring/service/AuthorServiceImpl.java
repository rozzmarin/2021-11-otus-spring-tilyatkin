package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao dao;

    @Override
    public Author find(AuthorId id) {
        return dao.get(id);
    }

    @Override
    public List<Author> find(AuthorFilter filter) {
        return dao.get(filter);
    }

    @Override
    public Author add(Author author) {
        AuthorId authorId = dao.insert(author);
        if (authorId != null) {
            return dao.get(authorId);
        }
        return null;
    }

    @Override
    public Author edit(Author author) {
        AuthorId authorId = dao.update(author);
        if (authorId != null) {
            return dao.get(authorId);
        }
        return null;
    }

    @Override
    public AuthorId remove(AuthorId id) {
        return dao.delete(id);
    }
}
