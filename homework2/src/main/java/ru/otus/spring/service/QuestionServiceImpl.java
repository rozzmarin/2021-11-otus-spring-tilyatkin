package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Question> getAllQuestions() {
        int questionsCount = this.dao.getCount();
        List<Question> questionList = new ArrayList<>(questionsCount);
        for (int i = 0; i < questionsCount; i++){
            Question question = this.dao.getByIndex(i);
            questionList.add(question);
        }
        return questionList;
    }
}
