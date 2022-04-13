package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreId;

public interface GenreRepository extends JpaRepository<Genre, GenreId>, JpaSpecificationExecutor<Genre> {
}
