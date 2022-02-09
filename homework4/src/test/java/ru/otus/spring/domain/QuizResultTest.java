package ru.otus.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuizResultTest {
    @Test
    void shouldHaveCorrectDefaultConstructor() {
        QuizResult quizResult = new QuizResult();

        Assertions.assertThat(quizResult.getAnswersCount())
                .isEqualTo(0);
        Assertions.assertThat(quizResult.getCorrectAnswersCount())
                .isEqualTo(0);
    }

    @Test
    void shouldHaveCorrectConstructorWithArguments() {
        QuizResult quizResult = new QuizResult(5, 3);

        Assertions.assertThat(quizResult.getAnswersCount())
                .isEqualTo(5);
        Assertions.assertThat(quizResult.getCorrectAnswersCount())
                .isEqualTo(3);
    }

    @Test
    void shouldHaveExceptionInConstructorWithArguments() {
        Assertions.assertThatThrownBy(() -> {
            new QuizResult(5, 7);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addCorrectAnswer() {
        QuizResult quizResult = new QuizResult(5, 3);
        quizResult.addAnswer(true);

        Assertions.assertThat(quizResult.getAnswersCount())
                .isEqualTo(6);
        Assertions.assertThat(quizResult.getCorrectAnswersCount())
                .isEqualTo(4);
    }

    @Test
    void addIncorrectAnswer() {
        QuizResult quizResult = new QuizResult(5, 3);
        quizResult.addAnswer(false);

        Assertions.assertThat(quizResult.getAnswersCount())
                .isEqualTo(6);
        Assertions.assertThat(quizResult.getCorrectAnswersCount())
                .isEqualTo(3);
    }
}
