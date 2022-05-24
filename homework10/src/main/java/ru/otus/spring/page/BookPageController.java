package ru.otus.spring.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.BookId;

@Controller
@RequiredArgsConstructor
public class BookPageController {
    @GetMapping(path = { "/books", "/" })
    public String readBooks(Model model) {
        return "books/list";
    }

    @GetMapping("/books/add")
    public String addBook(Model model) {
        return "books/edit";
    }

    @GetMapping("/books/{id}")
    public String editBook(@PathVariable("id") BookId id, Model model) {
        model.addAttribute("bookId", id);
        return "books/edit";
    }
}