package ru.otus.spring.service;

import ru.otus.spring.domain.QuizResult;

public interface QuizEngineService {
    QuizResult process();
    boolean canContinue(QuizResult quizResult);
}
