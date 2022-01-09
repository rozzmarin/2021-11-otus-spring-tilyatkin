package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

public interface QuestionDao {
    int getCount();
    Question getByIndex(int index);
}
