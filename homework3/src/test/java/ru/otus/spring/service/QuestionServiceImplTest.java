package ru.otus.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void getAllQuestions() {
        Question question1 = new Question("Q1", "Opt1", "Opt2");
        Question question2 = new Question("Q2");
        BDDMockito.given(questionDao.getCount())
                .willReturn(2);
        BDDMockito.given(questionDao.getByIndex(0))
                .willReturn(question1);
        BDDMockito.given(questionDao.getByIndex(1))
                .willReturn(question2);

        Assertions.assertThat(questionService.getAllQuestions())
                .hasSize(2)
                .containsOnly(question1, question2);
    }
}