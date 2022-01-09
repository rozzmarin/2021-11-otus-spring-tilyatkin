package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    public Question[] getAllQuestions() {
        int questionsCount = this.dao.getCount();
        Question[] questions = new Question[questionsCount];
        for (int i = 0; i < questionsCount; i++){
            Question question = this.dao.getByIndex(i);
            questions[i] = question;
        }
        return questions;
    }
}
