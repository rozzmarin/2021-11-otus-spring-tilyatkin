package ru.otus.spring.service;

import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;

public interface PrintQuizResultService {
    void print(Student student, QuizResult quizResult);
}
