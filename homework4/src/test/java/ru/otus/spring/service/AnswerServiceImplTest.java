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
import ru.otus.spring.dao.AnswerDao;
import ru.otus.spring.domain.Answer;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerServiceImplTest {
    @MockBean
    private AnswerDao answerDao;
    @Autowired
    private AnswerService answerService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        AnswerService answerService(AnswerDao answerDao) {
            return new AnswerServiceImpl(answerDao);
        }
    }

    @Test
    void checkCorrectAnswer() {
        Answer answer = new Answer("Answer");
        Answer correctAnswer = new Answer("Answer");
        BDDMockito.given(answerDao.getByIndex(0))
                .willReturn(answer);

        Assertions.assertThat(answerService.checkAnswer(0, correctAnswer))
                .isEqualTo(true);
    }

    @Test
    void checkIncorrectAnswer() {
        Answer answer = new Answer("Answer");
        Answer incorrectAnswer = new Answer("Incorrect");
        BDDMockito.given(answerDao.getByIndex(0))
                .willReturn(answer);

        Assertions.assertThat(answerService.checkAnswer(0, incorrectAnswer))
                .isEqualTo(false);
    }
}