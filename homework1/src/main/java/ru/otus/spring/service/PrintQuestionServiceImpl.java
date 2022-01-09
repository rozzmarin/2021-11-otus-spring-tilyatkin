package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

public class PrintQuestionServiceImpl implements PrintQuestionService {
    public void print(Question question) {
        System.out.println("Question: " + question.getBody());
        if (question.hasAnswerOptions()) {
            String[] answerOptions = question.getAnswerOptions();
            for (int i = 0; i < answerOptions.length; i++) {
                System.out.printf("%d. %s", i + 1, answerOptions[i]);
                System.out.println();
            }
        }
        System.out.println();
    }
}
