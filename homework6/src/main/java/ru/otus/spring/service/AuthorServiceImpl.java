package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Author find(AuthorId id) {
        return repository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> find(AuthorFilter filter) {
        return repository.get(filter);
    }

    @Override
    public Author add(Author author) {
        AuthorId authorId = repository.insert(author);
        if (authorId != null) {
            return repository.get(authorId);
        }
        return null;
    }

    @Override
    public Author edit(Author author) {
        AuthorId authorId = repository.update(author);
        if (authorId != null) {
            return repository.get(authorId);
        }
        return null;
    }

    @Override
    public AuthorId remove(AuthorId id) {
        return repository.delete(id);
    }
}
