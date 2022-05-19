package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorId;

public interface AuthorRepository extends JpaRepository<Author, AuthorId>, JpaSpecificationExecutor<Author> {
}
