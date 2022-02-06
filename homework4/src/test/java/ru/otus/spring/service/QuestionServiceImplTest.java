package ru.otus.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionServiceImplTest {
    @MockBean
    private QuestionDao questionDao;
    @Autowired
    private QuestionService questionService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        QuestionService questionService(QuestionDao questionDao) {
            return new QuestionServiceImpl(questionDao);
        }
    }

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