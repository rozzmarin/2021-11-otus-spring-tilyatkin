package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.AuthorId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDto {
    private AuthorId authorId;

    @NotNull(message = "Фамилия не может быть пустой")
    @Size(min = 1, max = 255, message = "Фамилия должна быть длиной от 1 до 255 символов")
    private String lastname;

    @NotNull(message = "Имя не может быть пустым")
    @Size(min = 1, max = 255, message = "Имя должно быть длиной от 1 до 255 символов")
    private String firstname;

    public Author toDomain() {
        return Author.builder()
                .authorId(authorId)
                .lastname(lastname)
                .firstname(firstname)
                .build();
    }

    public static AuthorDto fromDomain(Author author) {
        return new AuthorDto(
                author.getAuthorId(),
                author.getLastname(),
                author.getFirstname());
    }
}
