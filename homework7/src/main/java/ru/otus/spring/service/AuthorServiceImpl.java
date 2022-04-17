package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.AuthorSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Author find(AuthorId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Author with id %s is not found", id.getAuthorId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> find(AuthorFilter filter) {
        return repository.findAll(AuthorSpecification.of(filter));
    }

    @Override
    @Transactional
    public Author add(Author author) {
        return repository.save(author);
    }

    @Override
    @Transactional
    public Author edit(Author author) {
        return repository.save(author);
    }

    @Override
    @Transactional
    public AuthorId remove(AuthorId id) {
        repository.deleteById(id);
        return id;
    }
}
