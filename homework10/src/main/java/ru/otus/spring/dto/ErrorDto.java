package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDto {
    public String errorMessage;
    public List<String> errors;

    public ErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errors = List.of(errorMessage);
    }
}