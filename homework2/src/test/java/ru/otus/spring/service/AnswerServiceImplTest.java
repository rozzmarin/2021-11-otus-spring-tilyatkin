package ru.otus.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.AnswerDao;
import ru.otus.spring.domain.Answer;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceImplTest {
    @Mock
    private AnswerDao answerDao;
    @InjectMocks
    private AnswerServiceImpl answerService;

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