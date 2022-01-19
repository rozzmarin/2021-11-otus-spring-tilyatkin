package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.domain.Student;

import java.util.List;
import java.util.Scanner;

@Service
public class PrintQuizResultServiceImpl implements PrintQuizResultService {
    @Override
    public void print(Student student, QuizResult quizResult) {
        System.out.println();
        System.out.printf("%s, your result is %d correct answers of %d"
                , student.getName()
                , quizResult.getCorrectAnswersCount()
                , quizResult.getAnswersCount());
    }
}
