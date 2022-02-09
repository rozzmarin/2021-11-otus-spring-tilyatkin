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
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuizResult;
import ru.otus.spring.service.print.PrintQuestionService;
import ru.otus.spring.service.scan.ScanAnswerService;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuizEngineServiceImplTest {
    @MockBean
    private QuestionService questionService;
    @MockBean
    private PrintQuestionService printQuestionService;
    @MockBean
    private AnswerService answerService;
    @MockBean
    private ScanAnswerService scanAnswerService;
    @Autowired
    private QuizEngineService quizEngineService;

    @Configuration
    static class NestedConfiguration {
        @Bean
        QuizEngineService quizEngineService(QuestionService questionService,
                                            PrintQuestionService printQuestionService,
                                            AnswerService answerService,
                                            ScanAnswerService scanAnswerService) {
            return new QuizEngineServiceImpl(questionService,
                    printQuestionService,
                    answerService,
                    scanAnswerService);
        }
    }

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