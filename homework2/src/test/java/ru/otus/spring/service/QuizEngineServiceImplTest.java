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
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuizResult;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class QuizEngineServiceImplTest {
    @Mock
    private QuestionService questionService;
    @Mock
    private PrintQuestionService printQuestionService;
    @Mock
    private AnswerService answerService;
    @Mock
    private ScanAnswerService scanAnswerService;
    @InjectMocks
    private QuizEngineServiceImpl quizEngineService;

    @Test
    void canContinue() {
        QuizResult quizResult = new QuizResult(5, 3);
        Assertions.assertThat(quizEngineService.canContinue(quizResult))
                .isEqualTo(true);
    }

    @Test
    void canNotContinue() {
        QuizResult quizResult = new QuizResult(5, 5);
        Assertions.assertThat(quizEngineService.canContinue(quizResult))
                .isEqualTo(false);
    }

    @Test
    void process() {
        Question question1 = new Question("Q1", "Opt1", "Opt2", "Opt3", "Opt4");
        Question question2 = new Question("Q2", "Opt1", "Opt2");
        Question question3 = new Question("Q3");
        Answer answer1 = new Answer("Opt1");
        Answer answer2 = new Answer("Opt2");
        Answer answer3 = new Answer("Free");

        BDDMockito.given(questionService.getAllQuestions())
                .willReturn(List.of(question1, question2, question3));

        BDDMockito.given(scanAnswerService.scan(question1.getAnswerOptions()))
                .willReturn(answer1);
        BDDMockito.given(scanAnswerService.scan(question2.getAnswerOptions()))
                .willReturn(answer2);
        BDDMockito.given(scanAnswerService.scan(question3.getAnswerOptions()))
                .willReturn(answer3);

        BDDMockito.given(answerService.checkAnswer(0, answer1))
                .willReturn(true);
        BDDMockito.given(answerService.checkAnswer(1, answer2))
                .willReturn(false);
        BDDMockito.given(answerService.checkAnswer(2, answer3))
                .willReturn(true);

        QuizResult quizResult = quizEngineService.process();

        BDDMockito.then(printQuestionService)
                .should()
                .print(question1);
        BDDMockito.then(printQuestionService)
                .should()
                .print(question2);
        BDDMockito.then(printQuestionService)
                .should()
                .print(question3);

        Assertions.assertThat(quizResult)
                .isNotNull()
                .matches(r -> r.getAnswersCount() == 3)
                .matches(r -> r.getCorrectAnswersCount() == 2);
    }
}