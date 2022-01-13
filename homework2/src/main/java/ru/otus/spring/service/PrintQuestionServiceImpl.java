package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public class PrintQuestionServiceImpl implements PrintQuestionService {
    @Override
    public void print(Question question) {
        System.out.println("Question: " + question.getBody());
        if (question.hasAnswerOptions()) {
            List<String> answerOptions = question.getAnswerOptions();
            for (int i = 0; i < answerOptions.size(); i++) {
                System.out.printf("%d. %s", i + 1, answerOptions.get(i));
                System.out.println();
            }
        }
        System.out.println();
    }
}
