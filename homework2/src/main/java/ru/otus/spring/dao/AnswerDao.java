package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;

public interface AnswerDao {
    int getCount();
    Answer getByIndex(int index);
}
