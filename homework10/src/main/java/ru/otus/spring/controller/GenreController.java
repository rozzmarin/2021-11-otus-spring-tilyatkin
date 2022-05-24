package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.GenreFilter;
import ru.otus.spring.domain.GenreId;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<Genre> readGenres(GenreFilter genreFilter) {
        return genreService.find(genreFilter);
    }

    @GetMapping("/api/genres/{id}")
    public GenreDto readGenre(@PathVariable("id") GenreId id) {
        return GenreDto.fromDomain(genreService.find(id));
    }

    @PostMapping("/api/genres")
    public GenreDto addGenre(@RequestBody @Valid GenreDto genre) {
        return GenreDto.fromDomain(genreService.add(genre.toDomain()));
    }

    @PutMapping("/api/genres/{id}")
    public GenreDto editGenre(@PathVariable("id") GenreId id, @RequestBody @Valid GenreDto genre) {
        genre.setGenreId(id);
        return GenreDto.fromDomain(genreService.edit(genre.toDomain()));
    }

    @DeleteMapping("/api/genres/{id}")
    public GenreId removeGenre(@PathVariable("id") GenreId id) {
        return genreService.remove(id);
    }
}