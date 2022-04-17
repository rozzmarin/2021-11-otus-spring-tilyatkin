package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.BookFilter;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.exception.ObjectNotFoundException;
import ru.otus.spring.repository.specification.BaseSpecification;
import ru.otus.spring.service.idGenerator.AuthorDummyIdentifierGenerator;
import ru.otus.spring.service.idGenerator.IdentifierGenerator;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;
    private final BookRepository bookRepository;
    private final BaseSpecification<AuthorFilter> specification;
    private final BaseSpecification<BookFilter> bookSpecification;
    private final IdentifierGenerator<AuthorId> authorIdGenerator;

    @Override
    @Transactional(readOnly = true)
    public Author find(AuthorId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Author with id %s is not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> find(AuthorFilter filter) {
        return repository.findAll(specification.toPredicate(filter));
    }

    @Override
    public Author add(Author author) {
        author.setAuthorId(authorIdGenerator.generate());
        return repository.save(author);
    }

    @Override
    public Author edit(Author author) {
        return repository.save(author);
    }

    @Override
    public AuthorId remove(AuthorId id) {
        BookFilter booksByAuthorIdFilter = BookFilter.builder()
                .authorFilter(AuthorFilter.builder()
                        .authorIds(Set.of(id))
                        .build())
                .build();
        long booksWithAuthorIdCount = bookRepository.count(bookSpecification.toPredicate(booksByAuthorIdFilter));
        if (booksWithAuthorIdCount == 0) {
            repository.deleteById(id);
            return id;
        }
        return null;
    }
}
