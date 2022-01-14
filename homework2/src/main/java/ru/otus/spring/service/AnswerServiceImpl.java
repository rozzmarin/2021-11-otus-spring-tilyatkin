package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AnswerDao;
import ru.otus.spring.domain.Answer;

import java.util.Objects;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerDao dao;

    public AnswerServiceImpl(AnswerDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean checkAnswer(int index, Answer answer) {
        Answer primeAnswer = this.dao.getByIndex(index);
        return Objects.equals(primeAnswer, answer);
    }
}
