package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public class QuizServiceImpl implements QuizService {
    private final QuestionService questionService;
    private final PrintQuestionService printQuestionService;

    public QuizServiceImpl(QuestionService questionService, PrintQuestionService printQuestionService) {
        this.questionService = questionService;
        this.printQuestionService = printQuestionService;
    }

    @Override
    public void start() {
        List<Question> questions = this.questionService.getAllQuestions();
        for (Question question: questions) {
            this.printQuestionService.print(question);
        }
    }
}
