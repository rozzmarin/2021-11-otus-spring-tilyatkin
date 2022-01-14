package ru.otus.spring.service;

import ru.otus.spring.domain.Answer;

import java.util.List;

public interface ScanAnswerService {
    Answer scan(List<String> answerOptions);
}
