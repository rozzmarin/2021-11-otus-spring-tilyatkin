package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorFilter;
import ru.otus.spring.domain.AuthorId;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public String readAuthors(
            @ModelAttribute AuthorFilter authorFilter,
            Model model
    ) {
        List<Author> authors = authorService.find(authorFilter);
        model.addAttribute("authorFilter", authorFilter);
        model.addAttribute("authors", authors);
        return "authors/list";
    }

    @GetMapping("/authors/add")
    public String addAuthor(
            Model model
    ) {
        model.addAttribute("author", new AuthorDto());
        return "authors/add";
    }

    @PostMapping("/authors/add")
    public String addAuthor(
            @ModelAttribute("author") @Valid AuthorDto author,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "authors/add";
        }
        authorService.add(author.toDomain());
        return "redirect:/authors";
    }

    @GetMapping("/authors/{id}")
    public String editAuthor(
            @PathVariable("id") AuthorId id,
            Model model
    ) {
        AuthorDto author = AuthorDto.fromDomain(authorService.find(id));
        model.addAttribute("author", author);
        return "authors/edit";
    }

    @PostMapping("/authors/{id}")
    public String editAuthor(
            @PathVariable("id") AuthorId id,
            @ModelAttribute("author") @Valid AuthorDto author,
            BindingResult result,
            Model model
    ) {
        author.setAuthorId(id);
        if (result.hasErrors()) {
            return "authors/edit";
        }
        authorService.edit(author.toDomain());
        return "redirect:/authors";
    }

    @PostMapping("/authors/{id}/remove")
    public String removeAuthor(
            @PathVariable("id") AuthorId id
    ) {
        authorService.remove(id);
        return "redirect:/authors";
    }
}