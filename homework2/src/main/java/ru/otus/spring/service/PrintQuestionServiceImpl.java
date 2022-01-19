package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service
public class PrintQuestionServiceImpl implements PrintQuestionService {
    @Override
    public void print(Question question) {
        System.out.println();
        System.out.println("Question: " + question.getBody());
        if (question.hasAnswerOptions()) {
            List<String> answerOptions = question.getAnswerOptions();
            for (int i = 0; i < answerOptions.size(); i++) {
                System.out.printf("%d. %s", i + 1, answerOptions.get(i));
                System.out.println();
            }
        }
    }
}
