package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.exception.InvalidOperationException;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.domain.exception.ObjectNotFoundException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.specification.AuthorSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Author find(AuthorId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Автор с кодом %s не найден", id.getAuthorId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> find(AuthorFilter filter) {
        return repository.findAll(AuthorSpecification.of(filter));
    }

    @Override
    @Transactional
    public Author add(Author author) {
        if (author.getAuthorId() != null) {
            throw new InvalidOperationException("Недопустимый идентификатор");
        }
        return repository.save(author);
    }

    @Override
    @Transactional
    public Author edit(Author author) {
        if (author.getAuthorId() == null) {
            throw new InvalidOperationException("Не задан идентификатор");
        }
        if (repository.findById(author.getAuthorId()).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Автор с кодом %s не найден", author.getAuthorId().getAuthorId()));
        }
        return repository.save(author);
    }

    @Override
    @Transactional
    public AuthorId remove(AuthorId id) {
        if (repository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format("Автор с кодом %s не найден", id.getAuthorId()));
        }
        if (bookRepository.existsByAuthorsAuthorId(id)) {
            throw new InvalidOperationException("Нельзя удалить автора, у которого имеются книги");
        }
        repository.deleteById(id);
        return id;
    }
}