package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<Author> readAuthors(AuthorFilter authorFilter) {
        return authorService.find(authorFilter);
    }

    @GetMapping("/api/authors/{id}")
    public AuthorDto readAuthor(@PathVariable("id") AuthorId id) {
        return AuthorDto.fromDomain(authorService.find(id));
    }

    @PostMapping("/api/authors")
    public AuthorDto addAuthor(@RequestBody @Valid AuthorDto author) {
        return AuthorDto.fromDomain(authorService.add(author.toDomain()));
    }

    @PutMapping("/api/authors/{id}")
    public AuthorDto editAuthor(@PathVariable("id") AuthorId id, @RequestBody @Valid AuthorDto author) {
        author.setAuthorId(id);
        return AuthorDto.fromDomain(authorService.edit(author.toDomain()));
    }

    @DeleteMapping("/api/authors/{id}")
    public AuthorId removeAuthor(@PathVariable("id") AuthorId id) {
        return authorService.remove(id);
    }
}