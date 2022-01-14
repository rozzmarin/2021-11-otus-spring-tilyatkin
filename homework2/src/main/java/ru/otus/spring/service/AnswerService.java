package ru.otus.spring.service;

import ru.otus.spring.domain.Answer;

public interface AnswerService {
    boolean checkAnswer(int index, Answer answer);
}
